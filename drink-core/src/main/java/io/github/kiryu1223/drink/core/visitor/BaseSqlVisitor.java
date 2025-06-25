package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlValueExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.expression.SqlOperator;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.sqlExt.SqlExtensionExpression;
import io.github.kiryu1223.drink.base.transform.*;
import io.github.kiryu1223.drink.core.exception.SqLinkIllegalExpressionException;
import io.github.kiryu1223.drink.core.exception.SqlFuncExtNotFoundException;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.github.kiryu1223.drink.core.util.ExpressionUtil.isVoid;

public abstract class BaseSqlVisitor extends ResultThrowVisitor<ISqlExpression> {
    protected final IConfig config;
    protected final SqlExpressionFactory factory;
    protected final Transformer transformer;

    protected BaseSqlVisitor(IConfig config) {
        this.config = config;
        factory = config.getSqlExpressionFactory();
        transformer = config.getTransformer();
    }

    protected ISqlExpression mathHandler(MethodCallExpression methodCall) {
        Method method = methodCall.getMethod();
        IMathMethods math = config.getTransformer();
        List<Expression> args = methodCall.getArgs();
        switch (method.getName()) {
            case "abs": {
                ISqlExpression arg = visit(args.get(0));
                return math.abs(arg);
            }
            case "cos": {
                ISqlExpression arg = visit(args.get(0));
                return math.cos(arg);
            }
            case "acos": {
                ISqlExpression arg = visit(args.get(0));
                return math.acos(arg);
            }
            case "sin": {
                ISqlExpression arg = visit(args.get(0));
                return math.sin(arg);
            }
            case "asin": {
                ISqlExpression arg = visit(args.get(0));
                return math.asin(arg);
            }
            case "tan": {
                ISqlExpression arg = visit(args.get(0));
                return math.tan(arg);
            }
            case "atan": {
                ISqlExpression arg = visit(args.get(0));
                return math.atan(arg);
            }
            case "atan2": {
                ISqlExpression arg1 = visit(args.get(0));
                ISqlExpression arg2 = visit(args.get(1));
                return math.atan2(arg1, arg2);
            }
            case "toDegrees": {
                ISqlExpression arg = visit(args.get(0));
                return math.toDegrees(arg);
            }
            case "toRadians": {
                ISqlExpression arg = visit(args.get(0));
                return math.toRadians(arg);
            }
            case "exp": {
                ISqlExpression arg = visit(args.get(0));
                return math.exp(arg);
            }
            case "floor": {
                ISqlExpression arg = visit(args.get(0));
                return math.floor(arg);
            }
            case "log": {
                ISqlExpression arg = visit(args.get(0));
                return math.log(arg);
            }
            case "log10": {
                ISqlExpression arg = visit(args.get(0));
                return math.log10(arg);
            }
            case "random": {
                return math.random();
            }
            case "round": {
                ISqlExpression arg = visit(args.get(0));
                return math.round(arg);
            }
            case "pow": {
                ISqlExpression arg1 = visit(args.get(0));
                ISqlExpression arg2 = visit(args.get(1));
                return math.pow(arg1,arg2);
            }
            case "signum": {
                ISqlExpression arg = visit(args.get(0));
                return math.signum(arg);
            }
            case "sqrt": {
                ISqlExpression arg = visit(args.get(0));
                return math.sqrt(arg);
            }
            default:
                return checkAndReturnValue(methodCall);
        }
    }

    protected ISqlExpression stringHandler(MethodCallExpression methodCall) {
        Method method = methodCall.getMethod();
        IStringMethods str = config.getTransformer();
        if (Modifier.isStatic(method.getModifiers())) {
            switch (method.getName()) {
                case "join": {
                    ISqlExpression delimiter = visit(methodCall.getArgs().get(0));
                    //String.join(CharSequence delimiter, CharSequence... elements)
                    if (method.isVarArgs()) {
                        List<ISqlExpression> args = new ArrayList<>(methodCall.getArgs().size() - 1);
                        for (int i = 1; i < methodCall.getArgs().size(); i++) {
                            args.add(visit(methodCall.getArgs().get(i)));
                        }
                        return str.joinArray(delimiter, args);
                    }
                    else {
                        ISqlExpression elements = visit(methodCall.getArgs().get(1));
                        return str.joinList(delimiter, elements);
                    }
                }
                default:
                    return checkAndReturnValue(methodCall);
            }
        }
        else {
            switch (method.getName()) {
                case "contains": {
                    ISqlExpression left = visit(methodCall.getExpr());
                    ISqlExpression right = visit(methodCall.getArgs().get(0));
                    return str.contains(left, right);
                }
                case "startsWith": {
                    ISqlExpression left = visit(methodCall.getExpr());
                    ISqlExpression right = visit(methodCall.getArgs().get(0));
                    return str.startsWith(left, right);
                }
                case "endsWith": {
                    ISqlExpression left = visit(methodCall.getExpr());
                    ISqlExpression right = visit(methodCall.getArgs().get(0));
                    return str.endsWith(left, right);
                }
                case "length": {
                    ISqlExpression left = visit(methodCall.getExpr());
                    return str.length(left);
                }
                case "toUpperCase": {
                    ISqlExpression left = visit(methodCall.getExpr());
                    return str.toUpperCase(left);
                }
                case "toLowerCase": {
                    ISqlExpression left = visit(methodCall.getExpr());
                    return str.toLowerCase(left);
                }
                case "concat": {
                    ISqlExpression left = visit(methodCall.getExpr());
                    ISqlExpression right = visit(methodCall.getArgs().get(0));
                    return str.concat(left, right);
                }
                case "trim": {
                    ISqlExpression left = visit(methodCall.getExpr());
                    return str.trim(left);
                }
                case "isEmpty": {
                    ISqlExpression left = visit(methodCall.getExpr());
                    return str.isEmpty(left);
                }
                case "indexOf": {
                    if (method.getParameterTypes()[0] == String.class) {
                        ISqlExpression thisStr = visit(methodCall.getExpr());
                        ISqlExpression subStr = visit(methodCall.getArgs().get(0));
                        if (method.getParameterCount() == 1) {
                            return str.indexOf(thisStr, subStr);
                        }
                        else {
                            ISqlExpression fromIndex = visit(methodCall.getArgs().get(1));
                            return str.indexOf(thisStr, subStr, fromIndex);
                        }
                    }
                }
                case "replace": {
                    ISqlExpression thisStr = visit(methodCall.getExpr());
                    ISqlExpression oldStr = visit(methodCall.getArgs().get(0));
                    ISqlExpression newStr = visit(methodCall.getArgs().get(1));
                    return str.replace(thisStr, oldStr, newStr);
                }
                case "substring": {
                    ISqlExpression thisStr = visit(methodCall.getExpr());
                    ISqlExpression beginIndex = visit(methodCall.getArgs().get(0));
                    if (method.getParameterCount() == 1) {
                        return str.substring(thisStr, beginIndex);
                    }
                    else {
                        ISqlExpression endIndex = visit(methodCall.getArgs().get(1));
                        return str.substring(thisStr, beginIndex, endIndex);
                    }
                }
                default:
                    return checkAndReturnValue(methodCall);
            }
        }
    }

    protected ISqlExpression bigNumberHandler(MethodCallExpression methodCall) {
        Method method = methodCall.getMethod();
        INumberMethods number = config.getTransformer();
        switch (method.getName()) {
            case "add": {
                if (method.getParameterCount() == 1) {
                    ISqlExpression left = visit(methodCall.getExpr());
                    ISqlExpression right = visit(methodCall.getArgs().get(0));
                    return factory.binary(SqlOperator.PLUS, left, right);
                }
            }
            case "subtract": {
                if (method.getParameterCount() == 1) {
                    ISqlExpression left = visit(methodCall.getExpr());
                    ISqlExpression right = visit(methodCall.getArgs().get(0));
                    return factory.binary(SqlOperator.MINUS, left, right);
                }
            }
            case "multiply": {
                if (method.getParameterCount() == 1) {
                    ISqlExpression left = visit(methodCall.getExpr());
                    ISqlExpression right = visit(methodCall.getArgs().get(0));
                    return factory.binary(SqlOperator.MUL, left, right);
                }
            }
            case "divide": {
                if (method.getParameterCount() == 1) {
                    ISqlExpression left = visit(methodCall.getExpr());
                    ISqlExpression right = visit(methodCall.getArgs().get(0));
                    return factory.binary(SqlOperator.DIV, left, right);
                }
            }
            case "remainder": {
                if (method.getParameterCount() == 1) {
                    ISqlExpression left = visit(methodCall.getExpr());
                    ISqlExpression right = visit(methodCall.getArgs().get(0));
                    return number.remainder(left, right);
                }
            }
            default:
                return checkAndReturnValue(methodCall);
        }
    }

    protected ISqlExpression dateTimeHandler(MethodCallExpression methodCall) {
        Method method = methodCall.getMethod();
        IDateTimeMethods time = config.getTransformer();
        switch (method.getName()) {
            case "isAfter": {
                ISqlExpression left = visit(methodCall.getExpr());
                ISqlExpression right = visit(methodCall.getArgs().get(0));
                return time.isAfter(left, right);
            }
            case "isBefore": {
                ISqlExpression left = visit(methodCall.getExpr());
                ISqlExpression right = visit(methodCall.getArgs().get(0));
                return time.isBefore(left, right);
            }
            case "isEqual": {
                ISqlExpression left = visit(methodCall.getExpr());
                ISqlExpression right = visit(methodCall.getArgs().get(0));
                return time.isEqual(left, right);
            }
            default:
                return checkAndReturnValue(methodCall);
        }
    }

    protected ISqlExpression objectsHandler(MethodCallExpression methodCall) {
        Method method = methodCall.getMethod();
        IObjectsMethods objects = config.getTransformer();
        if (method.getName().equals("equals")) {
            List<Expression> args = methodCall.getArgs();
            return factory.binary(SqlOperator.EQ, visit(args.get(0)), visit(args.get(1)));
        }
        else if (method.getName().equals("nonNull")) {
            return objects.notNull(visit(methodCall.getArgs().get(0)));
        }
        else {
            return checkAndReturnValue(methodCall);
        }
    }

    protected ISqlValueExpression checkAndReturnValue(MethodCallExpression expression) {
        Method method = expression.getMethod();
        if (isVoid(method.getReturnType()) || hasParameter(expression)) {
            throw new SqLinkIllegalExpressionException(expression);
        }
        return factory.AnyValue(expression.getValue());
    }

    protected ISqlValueExpression checkAndReturnValue(FieldSelectExpression expression) {
        if (hasParameter(expression)) throw new SqLinkIllegalExpressionException(expression);
        return factory.AnyValue(expression.getValue());
    }

    protected ISqlValueExpression checkAndReturnValue(NewExpression expression) {
        if (hasParameter(expression)) throw new SqLinkIllegalExpressionException(expression);
        return factory.AnyValue(expression.getValue());
    }

    protected boolean hasParameter(Expression expression) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        expression.accept(new DeepFindVisitor() {
            @Override
            public void visit(ParameterExpression parameterExpression) {
                atomicBoolean.set(true);
            }
        });
        return atomicBoolean.get();
    }

    protected SqlExtensionExpression getSqlFuncExt(SqlExtensionExpression[] sqlExtensionExpressions) {
        DbType dbType = config.getDbType();
        Optional<SqlExtensionExpression> match = Arrays.stream(sqlExtensionExpressions).filter(a -> Arrays.stream(a.dbType()).anyMatch(b -> b == dbType)).findAny();
        if (!match.isPresent()) {
            Optional<SqlExtensionExpression> any = Arrays.stream(sqlExtensionExpressions).filter(a -> Arrays.stream(a.dbType()).anyMatch(b -> b == DbType.Any)).findAny();
            if (any.isPresent()) {
                return any.get();
            }
            throw new SqlFuncExtNotFoundException(dbType);
        }
        else {
            return match.get();
        }
    }

    protected static class JavaType implements ISqlExpression {

        private final Class<?> javaType;

        public JavaType(Class<?> javaType) {
            this.javaType = javaType;
        }

        @Override
        public String getSqlAndValue(IConfig config, List<SqlValue> values) {
            return "";
        }

        @Override
        public <T extends ISqlExpression> T copy(IConfig config) {
            return null;
        }

        public Class<?> getJavaType() {
            return javaType;
        }
    }
}
