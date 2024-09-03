package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.annotation.SqlExtensionExpression;
import io.github.kiryu1223.drink.annotation.SqlOperatorMethod;
import io.github.kiryu1223.drink.api.crud.read.group.IAggregation;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlOperator;
import io.github.kiryu1223.drink.core.expression.SqlColumnExpression;
import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlFunctionExpression;
import io.github.kiryu1223.drink.core.expression.SqlValueExpression;
import io.github.kiryu1223.drink.core.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.exception.IllegalExpressionException;
import io.github.kiryu1223.drink.exception.SqlFuncExtNotFoundException;
import io.github.kiryu1223.drink.ext.BaseSqlExtension;
import io.github.kiryu1223.drink.ext.BaseSqlExtensionCache;
import io.github.kiryu1223.drink.ext.DbType;
import io.github.kiryu1223.drink.ext.FunctionBox;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.*;

public abstract class SqlVisitor extends ResultThrowVisitor<SqlExpression>
{
    protected List<ParameterExpression> parameters;
    protected final Config config;
    protected final int offset;
    protected final SqlExpressionFactory factory;

    protected SqlVisitor(Config config)
    {
        this(config, 0);
    }

    public SqlVisitor(Config config, int offset)
    {
        this.config = config;
        this.offset = offset;
        this.factory = config.getSqlExpressionFactory();
    }

    @Override
    public SqlExpression visit(LambdaExpression<?> lambda)
    {
        if (parameters == null)
        {
            parameters = lambda.getParameters();
            return visit(lambda.getBody());
        }
        else
        {
            SqlVisitor self = getSelf();
            return self.visit(lambda);
        }
    }

    @Override
    public SqlExpression visit(AssignExpression assignExpression)
    {
        SqlExpression left = visit(assignExpression.getLeft());
        if (left instanceof SqlColumnExpression)
        {
            SqlColumnExpression sqlColumnExpression = (SqlColumnExpression) left;
            SqlExpression right = visit(assignExpression.getRight());
            return factory.set(sqlColumnExpression, right);
        }
        throw new RuntimeException("表达式中不能出现非lambda入参为赋值对象的语句");
    }

    @Override
    public SqlExpression visit(FieldSelectExpression fieldSelect)
    {
        if (isProperty(parameters, fieldSelect))
        {
            ParameterExpression parameter = (ParameterExpression) fieldSelect.getExpr();
            int index = parameters.indexOf(parameter) + offset;
            Field field = fieldSelect.getField();
            MetaData metaData = MetaDataCache.getMetaData(field.getDeclaringClass());
            return factory.column(metaData.getPropertyMetaData(field.getName()), index);
        }
        else
        {
            return checkAndReturnValue(fieldSelect);
        }
    }

    @Override
    public SqlExpression visit(MethodCallExpression methodCall)
    {
        if (IAggregation.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            String name = methodCall.getMethod().getName();
            switch (name)
            {
                case "count":
                    if (methodCall.getMethod().getParameterCount() == 0)
                    {
                        return factory.function(Collections.singletonList("COUNT(*)"), Collections.emptyList());
                    }
                case "sum":
                case "avg":
                case "max":
                case "min":
                    List<SqlExpression> args = new ArrayList<>(methodCall.getArgs().size());
                    for (Expression arg : methodCall.getArgs())
                    {
                        args.add(visit(arg));
                    }
                    List<String> strings = new ArrayList<>(args.size() + 1);
                    strings.add(name.toUpperCase() + "(");
                    for (int i = 0; i < args.size() - 1; i++)
                    {
                        strings.add(",");
                    }
                    strings.add(")");
                    return factory.function(strings, args);
                default:
                    throw new IllegalExpressionException(methodCall);
            }
        }
        else if (isSqlExtensionExpressionMethod(methodCall.getMethod()))
        {
            Method sqlFunction = methodCall.getMethod();
            SqlExtensionExpression sqlFuncExt = getSqlFuncExt(sqlFunction.getAnnotationsByType(SqlExtensionExpression.class));
            List<Expression> args = methodCall.getArgs();
            List<SqlExpression> expressions = new ArrayList<>(args.size());

            if (sqlFuncExt.extension() != BaseSqlExtension.class)
            {
                for (Expression arg : args)
                {
                    expressions.add(visit(arg));
                }
                BaseSqlExtension baseSqlExtension = BaseSqlExtensionCache.get(sqlFuncExt.extension());
                FunctionBox parse = baseSqlExtension.parse(sqlFunction, expressions);
                return factory.function(parse.getFunctions(), parse.getSqlExpressions());
            }
            else
            {
                List<String> strings = new ArrayList<>();
                List<Parameter> methodParameters = Arrays.stream(methodCall.getMethod().getParameters()).collect(Collectors.toList());
                ParamMatcher match = match(sqlFuncExt.function());
                List<String> functions = match.remainder;
                List<String> params = match.bracesContent;
                for (int i = 0; i < functions.size(); i++)
                {
                    strings.add(functions.get(i));
                    if (i < params.size())
                    {
                        String param = params.get(i);
                        Parameter targetParam = methodParameters.stream().filter(f -> f.getName().equals(param)).findFirst().get();
                        int index = methodParameters.indexOf(targetParam);

                        // 如果是可变参数
                        if (targetParam.isVarArgs())
                        {
                            while (index < args.size())
                            {
                                expressions.add(visit(args.get(index)));
                                if (index < args.size() - 1) strings.add(sqlFuncExt.separator());
                                index++;
                            }
                        }
                        // 正常情况
                        else
                        {
                            expressions.add(visit(args.get(index)));
                        }
                    }
                }
                return factory.function(strings, expressions);
            }


//            Method method = methodCall.getMethod();
//            List<Expression> args = methodCall.getArgs();
//            List<SqlExpression> contexts = new ArrayList<>(args.size());
//            SqlExtensionExpression[] sqlExtensionExpressions = method.getAnnotationsByType(SqlExtensionExpression.class);
//            if (sqlExtensionExpressions.length == 0)
//            {
//                List<String> strings = new ArrayList<>();
//                strings.add(method.getName() + "(");
//                for (int i = 0; i < args.size(); i++)
//                {
//                    Expression arg = args.get(i);
//                    contexts.add(visit(arg));
//                    if (i < args.size() - 1) strings.add(",");
//                }
//                strings.add(")");
//                return new SqlFunctionsContext(strings, contexts);
//            }
//            else
//            {
//                String function = getSqlFuncExt(sqlExtensionExpressions).function();
//                if (method.getParameterCount() == 0)
//                {
//                    return new SqlFunctionsContext(Collections.singletonList(function), Collections.emptyList());
//                }
//                else if (function.contains("{}"))
//                {
//                    List<String> strings = new ArrayList<>();
//                    String[] splitFunc = function.split("\\{}");
//                    for (int i = 0; i < splitFunc.length; i++)
//                    {
//                        strings.add(splitFunc[i]);
//                        // 可变参数情况
//                        if (i == splitFunc.length - 2
//                                && args.size() >= splitFunc.length)
//                        {
//                            while (i < args.size())
//                            {
//                                contexts.add(visit(args.get(i)));
//                                if (i < args.size() - 1) strings.add(",");
//                                i++;
//                            }
//                            strings.add(splitFunc[splitFunc.length - 1]);
//                        }
//                        // 正常情况
//                        else if (i < args.size()) contexts.add(visit(args.get(i)));
//                    }
//                    return new SqlFunctionsContext(strings, contexts);
//                }
//                else if (function.contains("{") && function.contains("}"))
//                {
//                    List<String> strings = new ArrayList<>();
//                    List<Parameter> methodParameters = Arrays.stream(methodCall.getMethod().getParameters()).collect(Collectors.toList());
//                    ParamMatcher match = match(function);
//                    List<String> functions = match.remainder;
//                    List<String> params = match.bracesContent;
//                    for (int i = 0; i < functions.size(); i++)
//                    {
//                        strings.add(functions.get(i));
//                        if (i < params.size())
//                        {
//                            String param = params.get(i);
//                            Parameter targetParam;
//                            int index;
//                            if (param.chars().allMatch(s -> Character.isDigit(s)))
//                            {
//                                //index形式
//                                index = Integer.parseInt(param);
//                                targetParam = methodParameters.get(index);
//                            }
//                            else
//                            {
//                                //arg名称形式
//                                targetParam = methodParameters.stream().filter(f -> f.getName().equals(param)).findFirst().get();
//                                index = methodParameters.indexOf(targetParam);
//                            }
//
//                            // 如果是可变参数
//                            if (targetParam.isVarArgs())
//                            {
//                                while (index < args.size())
//                                {
//                                    contexts.add(visit(args.get(index)));
//                                    if (index < args.size() - 1) strings.add(",");
//                                    index++;
//                                }
//                            }
//                            // 正常情况
//                            else
//                            {
//                                contexts.add(visit(args.get(index)));
//                            }
//                        }
//                    }
//                    return new SqlFunctionsContext(strings, contexts);
//                }
//                else
//                {
//                    throw new IllegalExpressionException(methodCall);
//                }
//            }
        }
        else if (Collection.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            Method method = methodCall.getMethod();
            if (method.getName().equals("contains"))
            {
                SqlExpression left = visit(methodCall.getArgs().get(0));
                SqlExpression right = visit(methodCall.getExpr());

                return factory.binary(SqlOperator.IN, left, right);
            }
            else
            {
                return checkAndReturnValue(methodCall);
            }
        }
        else if (String.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            Method method = methodCall.getMethod();
            switch (method.getName())
            {
                case "contains":
                {
                    SqlExpression left = visit(methodCall.getExpr());
                    SqlExpression right = visit(methodCall.getArgs().get(0));
                    SqlFunctionExpression function = factory.function(Arrays.asList("CONCAT('%',", ",'%')"), Collections.singletonList(right));
                    return factory.binary(SqlOperator.LIKE, left, function);
                }
                case "startsWith":
                {
                    SqlExpression left = visit(methodCall.getExpr());
                    SqlExpression right = visit(methodCall.getArgs().get(0));
                    SqlFunctionExpression function = factory.function(Arrays.asList("CONCAT(", ",'%')"), Collections.singletonList(right));
                    return factory.binary(SqlOperator.LIKE, left, function);
                }
                case "endsWith":
                {
                    SqlExpression left = visit(methodCall.getExpr());
                    SqlExpression right = visit(methodCall.getArgs().get(0));
                    SqlFunctionExpression function = factory.function(Arrays.asList("CONCAT('%',", ")"), Collections.singletonList(right));
                    return factory.binary(SqlOperator.LIKE, left, function);
                }
                default:
                    return checkAndReturnValue(methodCall);
            }
        }
        else if (Temporal.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            Method method = methodCall.getMethod();
            switch (method.getName())
            {
                case "isAfter":
                {
                    SqlExpression left = visit(methodCall.getExpr());
                    SqlExpression right = visit(methodCall.getArgs().get(0));
                    return factory.binary(SqlOperator.GT, left, right);
                }
                case "isBefore":
                {
                    SqlExpression left = visit(methodCall.getExpr());
                    SqlExpression right = visit(methodCall.getArgs().get(0));
                    return factory.binary(SqlOperator.LT, left, right);
                }
                case "isEqual":
                {
                    SqlExpression left = visit(methodCall.getExpr());
                    SqlExpression right = visit(methodCall.getArgs().get(0));
                    return factory.binary(SqlOperator.EQ, left, right);
                }
                default:
                    return checkAndReturnValue(methodCall);
            }
        }
        else if (isSqlOperatorMethod(methodCall.getMethod()))
        {
            Method method = methodCall.getMethod();
            List<Expression> args = methodCall.getArgs();
            SqlOperatorMethod operatorMethod = method.getAnnotation(SqlOperatorMethod.class);
            SqlOperator operator = operatorMethod.value();
            if (operator == SqlOperator.BETWEEN)
            {
                return factory.between(visit(args.get(0)), visit(args.get(1)), visit(args.get(2)));
            }
            else
            {
                if (operator.isLeft() || operator == SqlOperator.POSTINC || operator == SqlOperator.POSTDEC)
                {
                    return factory.unary(operator, visit(args.get(0)));
                }
                else
                {
                    SqlExpression left = visit(methodCall.getArgs().get(0));
                    SqlExpression right = visit(methodCall.getArgs().get(1));
                    return factory.binary(operator, left, right);
                }
            }
        }
        else if (isProperty(parameters, methodCall))
        {
            if (isGetter(methodCall.getMethod()))
            {
                ParameterExpression parameter = (ParameterExpression) methodCall.getExpr();
                int index = parameters.indexOf(parameter) + offset;
                Method getter = methodCall.getMethod();
                MetaData metaData = MetaDataCache.getMetaData(getter.getDeclaringClass());
                return factory.column(metaData.getPropertyMetaDataByGetter(getter), index);
            }
            else if (isSetter(methodCall.getMethod()))
            {
                ParameterExpression parameter = (ParameterExpression) methodCall.getExpr();
                int index = parameters.indexOf(parameter) + offset;
                Method setter = methodCall.getMethod();
                MetaData metaData = MetaDataCache.getMetaData(setter.getDeclaringClass());
                SqlColumnExpression columnExpression = factory.column(metaData.getPropertyMetaDataBySetter(setter), index);
                SqlExpression value = visit(methodCall.getArgs().get(0));
                return factory.set(columnExpression, value);
            }
            else
            {
                return checkAndReturnValue(methodCall);
            }
        }
        else
        {
            return checkAndReturnValue(methodCall);
        }
    }

    @Override
    public SqlExpression visit(BinaryExpression binary)
    {
        Expression left = binary.getLeft();
        Expression right = binary.getRight();
        return factory.binary(
                SqlOperator.valueOf(binary.getOperatorType().name()),
                visit(left),
                visit(right)
        );
    }

    @Override
    public SqlExpression visit(UnaryExpression unary)
    {
        return factory.unary(
                SqlOperator.valueOf(unary.getOperatorType().name()),
                visit(unary.getOperand())
        );
    }

    @Override
    public SqlExpression visit(ParensExpression parens)
    {
        return factory.parens(visit(parens.getExpr()));
    }

    @Override
    public SqlExpression visit(StaticClassExpression staticClass)
    {
        return factory.type(staticClass.getType());
    }

    @Override
    public SqlExpression visit(ConstantExpression constant)
    {
        return factory.value(constant.getValue());
    }

    @Override
    public SqlExpression visit(ReferenceExpression reference)
    {
        return factory.AnyValue(reference.getValue());
    }

    protected abstract SqlVisitor getSelf();

    protected SqlValueExpression checkAndReturnValue(MethodCallExpression expression)
    {
        Method method = expression.getMethod();
        if (isVoid(method.getReturnType()) || hasParameter(expression))
        {
            throw new IllegalExpressionException(expression);
        }
        return factory.AnyValue(expression.getValue());
    }

    protected SqlValueExpression checkAndReturnValue(FieldSelectExpression expression)
    {
        if (hasParameter(expression)) throw new IllegalExpressionException(expression);
        return factory.AnyValue(expression.getValue());
    }

    protected boolean hasParameter(Expression expression)
    {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        expression.accept(new DeepFindVisitor()
        {
            @Override
            public void visit(ParameterExpression parameterExpression)
            {
                atomicBoolean.set(true);
            }
        });
        return atomicBoolean.get();
    }

    protected SqlExtensionExpression getSqlFuncExt(SqlExtensionExpression[] sqlExtensionExpressions)
    {
        DbType dbType = config.getDbType();
        Optional<SqlExtensionExpression> first = Arrays.stream(sqlExtensionExpressions).filter(a -> a.dbType() == dbType).findFirst();
        if (!first.isPresent())
        {
            Optional<SqlExtensionExpression> any = Arrays.stream(sqlExtensionExpressions).filter(a -> a.dbType() == DbType.Any).findFirst();
            if (any.isPresent())
            {
                return any.get();
            }
            throw new SqlFuncExtNotFoundException(dbType);
        }
        else
        {
            return first.get();
        }
    }

    protected ParamMatcher match(String input)
    {
        ParamMatcher paramMatcher = new ParamMatcher();

        List<String> bracesContent = paramMatcher.bracesContent;
        List<String> remainder = paramMatcher.remainder;
        // 正则表达式匹配"{}"内的内容
        Pattern pattern = Pattern.compile("\\{([^}]+)}");
        Matcher matcher = pattern.matcher(input);

        int lastIndex = 0; // 上一个匹配项结束的位置
        while (matcher.find())
        {
            // 添加上一个匹配项到剩余字符串（如果有的话）
            if (lastIndex < matcher.start())
            {
                remainder.add(input.substring(lastIndex, matcher.start()));
            }

            // 提取并添加"{}"内的内容
            bracesContent.add(matcher.group(1));

            // 更新上一个匹配项结束的位置
            lastIndex = matcher.end();
        }

        // 添加最后一个匹配项之后的剩余字符串（如果有的话）
        if (lastIndex < input.length())
        {
            remainder.add(input.substring(lastIndex));
        }

        if (input.startsWith("{")) remainder.add(0, "");
        if (input.endsWith("}")) remainder.add("");

        return paramMatcher;
    }
}
