package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.annotation.SqlExtensionExpression;
import io.github.kiryu1223.drink.annotation.SqlOperatorMethod;
import io.github.kiryu1223.drink.api.crud.read.group.IAggregation;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.*;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.visitor.methods.BigDecimalMethods;
import io.github.kiryu1223.drink.core.visitor.methods.MathMethods;
import io.github.kiryu1223.drink.core.visitor.methods.StringMethods;
import io.github.kiryu1223.drink.core.visitor.methods.TemporalMethods;
import io.github.kiryu1223.drink.exception.DrinkException;
import io.github.kiryu1223.drink.exception.IllegalExpressionException;
import io.github.kiryu1223.drink.exception.SqlFuncExtNotFoundException;
import io.github.kiryu1223.drink.nnnn.expression.SqlColumnExpression;
import io.github.kiryu1223.drink.nnnn.expression.SqlValueExpression;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
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
        throw new DrinkException("表达式中不能出现非lambda入参为赋值对象的语句");
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
            return factory.column(metaData.getPropertyMetaDataByFieldName(field.getName()), index);
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
                        return factory.template(Collections.singletonList("COUNT(*)"), Collections.emptyList());
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
                    return factory.template(strings, args);
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
                BaseSqlExtension sqlExtension = BaseSqlExtension.getCache(sqlFuncExt.extension());
                return sqlExtension.parse(config,sqlFunction,expressions);
            }
            else
            {
                List<String> strings = new ArrayList<>();
                List<Parameter> methodParameters = Arrays.stream(methodCall.getMethod().getParameters()).collect(Collectors.toList());
                ParamMatcher match = match(sqlFuncExt.template());
                List<String> functions = match.remainder;
                List<String> params = match.bracesContent;
                for (int i = 0; i < functions.size(); i++)
                {
                    strings.add(functions.get(i));
                    if (i < params.size())
                    {
                        String param = params.get(i);
                        Parameter targetParam = methodParameters.stream()
                                .filter(f -> f.getName().equals(param))
                                .findFirst()
                                .orElseThrow(() -> new DrinkException("无法在" + sqlFuncExt.template() + "中找到" + param));
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
                return factory.template(strings, expressions);
            }
        }
        else if (List.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
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
            if (Modifier.isStatic(method.getModifiers()))
            {
                switch (method.getName())
                {
                    case "join":
                    {
                        SqlExpression delimiter = visit(methodCall.getArgs().get(0));
                        //String.join(CharSequence delimiter, CharSequence... elements)
                        if (method.isVarArgs())
                        {
                            List<SqlExpression> args = new ArrayList<>(methodCall.getArgs().size() - 1);
                            for (int i = 1; i < methodCall.getArgs().size(); i++)
                            {
                                args.add(visit(methodCall.getArgs().get(i)));
                            }
                            return StringMethods.joinArray(config, delimiter, args);
                        }
                        else
                        {
                            SqlExpression elements = visit(methodCall.getArgs().get(1));
                            return StringMethods.joinList(config, delimiter, elements);
                        }
                    }
                    default:
                        return checkAndReturnValue(methodCall);
                }
            }
            else
            {
                switch (method.getName())
                {
                    case "contains":
                    {
                        SqlExpression left = visit(methodCall.getExpr());
                        SqlExpression right = visit(methodCall.getArgs().get(0));
                        return StringMethods.contains(config, left, right);
                    }
                    case "startsWith":
                    {
                        SqlExpression left = visit(methodCall.getExpr());
                        SqlExpression right = visit(methodCall.getArgs().get(0));
                        return StringMethods.startsWith(config, left, right);
                    }
                    case "endsWith":
                    {
                        SqlExpression left = visit(methodCall.getExpr());
                        SqlExpression right = visit(methodCall.getArgs().get(0));
                        return StringMethods.endsWith(config, left, right);
                    }
                    case "length":
                    {
                        SqlExpression left = visit(methodCall.getExpr());
                        return StringMethods.length(config, left);
                    }
                    case "toUpperCase":
                    {
                        SqlExpression left = visit(methodCall.getExpr());
                        return StringMethods.toUpperCase(config, left);
                    }
                    case "toLowerCase":
                    {
                        SqlExpression left = visit(methodCall.getExpr());
                        return StringMethods.toLowerCase(config, left);
                    }
                    case "concat":
                    {
                        SqlExpression left = visit(methodCall.getExpr());
                        SqlExpression right = visit(methodCall.getArgs().get(0));
                        return StringMethods.concat(config, left, right);
                    }
                    case "trim":
                    {
                        SqlExpression left = visit(methodCall.getExpr());
                        return StringMethods.trim(config, left);
                    }
                    case "isEmpty":
                    {
                        SqlExpression left = visit(methodCall.getExpr());
                        return StringMethods.isEmpty(config, left);
                    }
                    case "indexOf":
                    {
                        if (method.getParameterTypes()[0] == String.class)
                        {
                            SqlExpression thisStr = visit(methodCall.getExpr());
                            SqlExpression subStr = visit(methodCall.getArgs().get(0));
                            if (method.getParameterCount() == 1)
                            {
                                return StringMethods.indexOf(config, thisStr, subStr);
                            }
                            else
                            {
                                SqlExpression fromIndex = visit(methodCall.getArgs().get(1));
                                return StringMethods.indexOf(config, thisStr, subStr, fromIndex);
                            }
                        }
                    }
                    case "replace":
                    {
                        SqlExpression thisStr = visit(methodCall.getExpr());
                        SqlExpression oldStr = visit(methodCall.getArgs().get(0));
                        SqlExpression newStr = visit(methodCall.getArgs().get(1));
                        return StringMethods.replace(config, thisStr, oldStr, newStr);
                    }
                    case "substring":
                    {
                        SqlExpression thisStr = visit(methodCall.getExpr());
                        SqlExpression beginIndex = visit(methodCall.getArgs().get(0));
                        if (method.getParameterCount() == 1)
                        {
                            return StringMethods.substring(config, thisStr, beginIndex);
                        }
                        else
                        {
                            SqlExpression endIndex = visit(methodCall.getArgs().get(1));
                            return StringMethods.substring(config, thisStr, beginIndex, endIndex);
                        }
                    }
                    default:
                        return checkAndReturnValue(methodCall);
                }
            }
        }
        else if (Math.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            Method method = methodCall.getMethod();
            switch (method.getName())
            {
                case "abs":
                {
                    SqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("ABS(", ")"), Collections.singletonList(arg));
                }
                case "cos":
                {
                    SqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("COS(", ")"), Collections.singletonList(arg));
                }
                case "acos":
                {
                    SqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("ACOS(", ")"), Collections.singletonList(arg));
                }
                case "sin":
                {
                    SqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("SIN(", ")"), Collections.singletonList(arg));
                }
                case "asin":
                {
                    SqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("ASIN(", ")"), Collections.singletonList(arg));
                }
                case "tan":
                {
                    SqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("TAN(", ")"), Collections.singletonList(arg));
                }
                case "atan":
                {
                    SqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("ATAN(", ")"), Collections.singletonList(arg));
                }
                case "atan2":
                {
                    SqlExpression arg1 = visit(methodCall.getArgs().get(0));
                    SqlExpression arg2 = visit(methodCall.getArgs().get(1));
                    return MathMethods.atan2(config, arg1, arg2);
                }
                case "toDegrees":
                {
                    SqlExpression arg = visit(methodCall.getArgs().get(0));
                    return MathMethods.toDegrees(config, arg);
                }
                case "toRadians":
                {
                    SqlExpression arg = visit(methodCall.getArgs().get(0));
                    return MathMethods.toRadians(config, arg);
                }
                case "exp":
                {
                    SqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("EXP(", ")"), Collections.singletonList(arg));
                }
                case "floor":
                {
                    SqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("FLOOR(", ")"), Collections.singletonList(arg));
                }
                case "log":
                {
                    SqlExpression arg = visit(methodCall.getArgs().get(0));
                    return MathMethods.log(config, arg);
                }
                case "log10":
                {
                    SqlExpression arg = visit(methodCall.getArgs().get(0));
                    return MathMethods.log10(config, arg);
                }
                case "random":
                {
                    return MathMethods.random(config);
                }
                case "round":
                {
                    SqlExpression arg = visit(methodCall.getArgs().get(0));
                    return MathMethods.round(config, arg);
                }
                case "pow":
                {
                    SqlExpression arg1 = visit(methodCall.getArgs().get(0));
                    SqlExpression arg2 = visit(methodCall.getArgs().get(1));
                    return factory.template(Arrays.asList("POWER(", ",", ")"), Arrays.asList(arg1, arg2));
                }
                case "signum":
                {
                    SqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("SIGN(", ")"), Collections.singletonList(arg));
                }
                case "sqrt":
                {
                    SqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("SQRT(", ")"), Collections.singletonList(arg));
                }
                default:
                    return checkAndReturnValue(methodCall);
            }
        }
        else if (BigDecimal.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            Method method = methodCall.getMethod();
            switch (method.getName())
            {
                case "add":
                {
                    if (method.getParameterCount() == 1)
                    {
                        SqlExpression left = visit(methodCall.getExpr());
                        SqlExpression right = visit(methodCall.getArgs().get(0));
                        return factory.binary(SqlOperator.PLUS, left, right);
                    }
                }
                case "subtract":
                {
                    if (method.getParameterCount() == 1)
                    {
                        SqlExpression left = visit(methodCall.getExpr());
                        SqlExpression right = visit(methodCall.getArgs().get(0));
                        return factory.binary(SqlOperator.MINUS, left, right);
                    }
                }
                case "multiply":
                {
                    if (method.getParameterCount() == 1)
                    {
                        SqlExpression left = visit(methodCall.getExpr());
                        SqlExpression right = visit(methodCall.getArgs().get(0));
                        return factory.binary(SqlOperator.MUL, left, right);
                    }
                }
                case "divide":
                {
                    if (method.getParameterCount() == 1)
                    {
                        SqlExpression left = visit(methodCall.getExpr());
                        SqlExpression right = visit(methodCall.getArgs().get(0));
                        return factory.binary(SqlOperator.DIV, left, right);
                    }
                }
                case "remainder":
                {
                    if (method.getParameterCount() == 1)
                    {
                        SqlExpression left = visit(methodCall.getExpr());
                        SqlExpression right = visit(methodCall.getArgs().get(0));
                        return BigDecimalMethods.remainder(config, left, right);
                    }
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
                    return TemporalMethods.isAfter(config, left, right);
                }
                case "isBefore":
                {
                    SqlExpression left = visit(methodCall.getExpr());
                    SqlExpression right = visit(methodCall.getArgs().get(0));
                    return TemporalMethods.isBefore(config, left, right);
                }
                case "isEqual":
                {
                    SqlExpression left = visit(methodCall.getExpr());
                    SqlExpression right = visit(methodCall.getArgs().get(0));
                    return TemporalMethods.isEqual(config, left, right);
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
                SqlExpression thiz = visit(args.get(0));
                SqlExpression min = visit(args.get(1));
                SqlExpression max = visit(args.get(2));
                return factory.binary(SqlOperator.BETWEEN, thiz, factory.binary(SqlOperator.AND, min, max));
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

    @Override
    public SqlExpression visit(NewExpression newExpression)
    {
        return checkAndReturnValue(newExpression);
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

    protected SqlValueExpression checkAndReturnValue(NewExpression expression)
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
