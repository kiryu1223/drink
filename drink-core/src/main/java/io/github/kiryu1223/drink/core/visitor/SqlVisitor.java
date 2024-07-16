package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.annotation.SqlFuncExt;
import io.github.kiryu1223.drink.annotation.SqlOperatorMethod;
import io.github.kiryu1223.drink.api.crud.read.group.IAggregation;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.*;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.exception.IllegalExpressionException;
import io.github.kiryu1223.drink.exception.SqlFuncExtNotFoundException;
import io.github.kiryu1223.drink.ext.DbType;
import io.github.kiryu1223.drink.ext.SqlFunctions;
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

public abstract class SqlVisitor extends ResultThrowVisitor<SqlContext>
{
    protected List<ParameterExpression> parameters;
    protected final Config config;

    protected SqlVisitor(Config config)
    {
        this.config = config;
    }

    @Override
    public SqlContext visit(LambdaExpression<?> lambda)
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
    public SqlContext visit(AssignExpression assignExpression)
    {
        SqlContext left = visit(assignExpression.getLeft());
        if (left instanceof SqlPropertyContext)
        {
            SqlPropertyContext sqlPropertyContext = (SqlPropertyContext) left;
            SqlContext right = visit(assignExpression.getRight());
            return new SqlSetContext(sqlPropertyContext, right);
        }
        throw new RuntimeException("表达式中不能出现非lambda入参为赋值对象的语句");
    }

    @Override
    public SqlContext visit(FieldSelectExpression fieldSelect)
    {
        if (isProperty(parameters, fieldSelect))
        {
            ParameterExpression parameter = (ParameterExpression) fieldSelect.getExpr();
            int index = parameters.indexOf(parameter);
            Field field = fieldSelect.getField();
            MetaData metaData = MetaDataCache.getMetaData(field.getDeclaringClass());
            return new SqlPropertyContext(metaData.getPropertyMetaData(field.getName()), index);
        }
        else
        {
            return checkAndReturnValue(fieldSelect);
        }
    }

    @Override
    public SqlContext visit(MethodCallExpression methodCall)
    {
        if (IAggregation.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            String name = methodCall.getMethod().getName();
            switch (name)
            {
                case "count":
                    if (methodCall.getMethod().getParameterCount() == 0)
                    {
                        return new SqlFuncContext("COUNT(*)", Collections.emptyList());
                    }
                case "sum":
                case "avg":
                case "max":
                case "min":
                    List<SqlContext> args = new ArrayList<>(methodCall.getArgs().size());
                    for (Expression arg : methodCall.getArgs())
                    {
                        args.add(visit(arg));
                    }
                    return new SqlFuncContext(name.toUpperCase(), args);
                default:
                    throw new IllegalExpressionException(methodCall);
            }
        }
        else if (SqlFunctions.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            Method method = methodCall.getMethod();
            List<Expression> args = methodCall.getArgs();
            List<SqlContext> contexts = new ArrayList<>(args.size());
            SqlFuncExt[] sqlFuncExts = method.getAnnotationsByType(SqlFuncExt.class);
            if (sqlFuncExts.length == 0)
            {
                List<String> strings = new ArrayList<>();
                strings.add(method.getName() + "(");
                for (int i = 0; i < args.size(); i++)
                {
                    Expression arg = args.get(i);
                    contexts.add(visit(arg));
                    if (i < args.size() - 1) strings.add(",");
                }
                strings.add(")");
                return new SqlFunctionsContext(strings, contexts);
            }
            else
            {
                String function = getSqlFuncExt(sqlFuncExts).function();
                if (method.getParameterCount() == 0)
                {
                    return new SqlFunctionsContext(Collections.singletonList(function), Collections.emptyList());
                }
                else if (function.contains("{}"))
                {
                    List<String> strings = new ArrayList<>();
                    String[] splitFunc = function.split("\\{}");
                    for (int i = 0; i < splitFunc.length; i++)
                    {
                        strings.add(splitFunc[i]);
                        // 可变参数情况
                        if (i == splitFunc.length - 2
                                && args.size() >= splitFunc.length)
                        {
                            while (i < args.size())
                            {
                                contexts.add(visit(args.get(i)));
                                if (i < args.size() - 1) strings.add(",");
                                i++;
                            }
                            strings.add(splitFunc[splitFunc.length - 1]);
                        }
                        // 正常情况
                        else if (i < args.size()) contexts.add(visit(args.get(i)));
                    }
                    return new SqlFunctionsContext(strings, contexts);
                }
                else if (function.contains("{") && function.contains("}"))
                {
                    List<String> strings = new ArrayList<>();
                    List<Parameter> methodParameters = Arrays.stream(methodCall.getMethod().getParameters()).collect(Collectors.toList());
                    ParamMatcher match = match(function);
                    List<String> functions = match.remainder;
                    List<String> params = match.bracesContent;
                    for (int i = 0; i < functions.size(); i++)
                    {
                        strings.add(functions.get(i));
                        if (i < params.size())
                        {
                            String param = params.get(i);
                            Parameter targetParam;
                            int index;
                            if (param.chars().allMatch(s -> Character.isDigit(s)))
                            {
                                //index形式
                                index = Integer.parseInt(param);
                                targetParam = methodParameters.get(index);
                            }
                            else
                            {
                                //arg名称形式
                                targetParam = methodParameters.stream().filter(f -> f.getName().equals(param)).findFirst().get();
                                index = methodParameters.indexOf(targetParam);
                            }

                            // 如果是可变参数
                            if (targetParam.isVarArgs())
                            {
                                while (index < args.size())
                                {
                                    contexts.add(visit(args.get(index)));
                                    if (index < args.size() - 1) strings.add(",");
                                    index++;
                                }
                            }
                            // 正常情况
                            else
                            {
                                contexts.add(visit(args.get(index)));
                            }
                        }
                    }
                    return new SqlFunctionsContext(strings, contexts);
                }
                else
                {
                    throw new IllegalExpressionException(methodCall);
                }
            }
        }
        else if (Collection.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            Method method = methodCall.getMethod();
            if (method.getName().equals("contains"))
            {
                SqlContext left = visit(methodCall.getArgs().get(0));
                SqlContext right = visit(methodCall.getExpr());

                return new SqlBinaryContext(SqlOperator.IN, left, new SqlParensContext(right));
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
                    SqlContext left = visit(methodCall.getExpr());
                    SqlContext right = visit(methodCall.getArgs().get(0));
                    SqlFunctionsContext functionsContext = new SqlFunctionsContext(Arrays.asList("CONCAT('%',", ",'%')"), Collections.singletonList(right));
                    return new SqlBinaryContext(SqlOperator.LIKE, left, functionsContext);
                }
                case "startsWith":
                {
                    SqlContext left = visit(methodCall.getExpr());
                    SqlContext right = visit(methodCall.getArgs().get(0));
                    SqlFunctionsContext functionsContext = new SqlFunctionsContext(Arrays.asList("CONCAT(", ",'%')"), Collections.singletonList(right));
                    return new SqlBinaryContext(SqlOperator.LIKE, left, functionsContext);
                }
                case "endsWith":
                {
                    SqlContext left = visit(methodCall.getExpr());
                    SqlContext right = visit(methodCall.getArgs().get(0));
                    SqlFunctionsContext functionsContext = new SqlFunctionsContext(Arrays.asList("CONCAT('%',", ")"), Collections.singletonList(right));
                    return new SqlBinaryContext(SqlOperator.LIKE, left, functionsContext);
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
                    SqlContext left = visit(methodCall.getExpr());
                    SqlContext right = visit(methodCall.getArgs().get(0));
                    return new SqlBinaryContext(SqlOperator.GT, left, right);
                }
                case "isBefore":
                {
                    SqlContext left = visit(methodCall.getExpr());
                    SqlContext right = visit(methodCall.getArgs().get(0));
                    return new SqlBinaryContext(SqlOperator.LT, left, right);
                }
                case "isEqual":
                {
                    SqlContext left = visit(methodCall.getExpr());
                    SqlContext right = visit(methodCall.getArgs().get(0));
                    return new SqlBinaryContext(SqlOperator.EQ, left, right);
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
                SqlContext min = visit(args.get(1));
                SqlContext max = visit(args.get(2));
                SqlBinaryContext and = new SqlBinaryContext(SqlOperator.AND, min, max);
                return new SqlBinaryContext(operator, visit(args.get(0)), and);
            }
//            else if (operator == SqlOperator.EXISTS)
//            {
//                QueryBase query = (QueryBase)args.get(0).getValue();
//                return new SqlUnaryContext(operator,new SqlParensContext(new SqlVirtualTableContext(query.getSqlBuilder())));
//            }
            else
            {
                if (operator.isLeft() || operator == SqlOperator.POSTINC || operator == SqlOperator.POSTDEC)
                {
                    return new SqlUnaryContext(operator, visit(methodCall.getArgs().get(0)));
                }
                else
                {
                    SqlContext left = visit(methodCall.getArgs().get(0));
                    SqlContext right = visit(methodCall.getArgs().get(1));
                    return new SqlBinaryContext(operator, left, right);
                }
            }
        }
        else if (isProperty(parameters, methodCall))
        {
            if (isGetter(methodCall.getMethod()))
            {
                ParameterExpression parameter = (ParameterExpression) methodCall.getExpr();
                int index = parameters.indexOf(parameter);
                Method getter = methodCall.getMethod();
                MetaData metaData = MetaDataCache.getMetaData(getter.getDeclaringClass());
                return new SqlPropertyContext(metaData.getPropertyMetaDataByGetter(getter), index);
            }
            else if (isSetter(methodCall.getMethod()))
            {
                ParameterExpression parameter = (ParameterExpression) methodCall.getExpr();
                int index = parameters.indexOf(parameter);
                Method setter = methodCall.getMethod();
                MetaData metaData = MetaDataCache.getMetaData(setter.getDeclaringClass());
                SqlPropertyContext propertyContext = new SqlPropertyContext(metaData.getPropertyMetaDataBySetter(setter), index);
                SqlContext context = visit(methodCall.getArgs().get(0));
                return new SqlSetContext(propertyContext, context);
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
    public SqlContext visit(BinaryExpression binary)
    {
        Expression left = binary.getLeft();
        Expression right = binary.getRight();
        return new SqlBinaryContext(
                SqlOperator.valueOf(binary.getOperatorType().name()),
                visit(left),
                visit(right)
        );
    }

    @Override
    public SqlContext visit(UnaryExpression unary)
    {
        return new SqlUnaryContext(
                SqlOperator.valueOf(unary.getOperatorType().name()),
                isNotAndHasntParens(unary)
                        ? new SqlParensContext(visit(unary.getOperand()))
                        : visit(unary.getOperand())
        );
    }

    @Override
    public SqlContext visit(ParensExpression parens)
    {
        return new SqlParensContext(visit(parens.getExpr()));
    }

    @Override
    public SqlContext visit(StaticClassExpression staticClass)
    {
        return new SqlTypeContext(staticClass.getType());
    }

    @Override
    public SqlContext visit(ConstantExpression constant)
    {
        return new SqlValueContext(constant.getValue());
    }

    @Override
    public SqlContext visit(ReferenceExpression reference)
    {
        return new SqlValueContext(reference.getValue());
    }

    protected abstract SqlVisitor getSelf();

    protected SqlValueContext checkAndReturnValue(MethodCallExpression expression)
    {
        Method method = expression.getMethod();
        if (isVoid(method.getReturnType()) || hasParameter(expression))
        {
            throw new IllegalExpressionException(expression);
        }
        return new SqlValueContext(expression.getValue());
    }

    protected SqlValueContext checkAndReturnValue(FieldSelectExpression expression)
    {
        if (hasParameter(expression)) throw new IllegalExpressionException(expression);
        return new SqlValueContext(expression.getValue());
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

    protected SqlFuncExt getSqlFuncExt(SqlFuncExt[] sqlFuncExts)
    {
        DbType dbType = config.getDbType();
        List<SqlFuncExt> collect = Arrays.stream(sqlFuncExts).filter(a -> a.dbType() == dbType).collect(Collectors.toList());
        if (collect.isEmpty())
        {
            throw new SqlFuncExtNotFoundException(dbType);
        }
        else
        {
            return collect.get(0);
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

        return paramMatcher;
    }

    private boolean isNotAndHasntParens(UnaryExpression unary)
    {
        return unary.getOperatorType() == OperatorType.NOT && unary.getOperand().getKind() != Kind.Parens;
    }
}
