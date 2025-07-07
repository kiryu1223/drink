package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.sqlExt.SqlExtensionExpression;
import io.github.kiryu1223.drink.base.transform.IAggregateMethods;
import io.github.kiryu1223.drink.base.transform.IMathMethods;
import io.github.kiryu1223.drink.base.transform.IStringMethods;
import io.github.kiryu1223.drink.base.transform.Transformer;
import io.github.kiryu1223.drink.core.exception.SqLinkException;
import io.github.kiryu1223.drink.core.exception.SqLinkIllegalExpressionException;
import io.github.kiryu1223.drink.core.exception.SqlFuncExtNotFoundException;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.isVoid;
import static io.github.kiryu1223.drink.base.util.DrinkUtil.last;

public abstract class BaseSqlVisitor extends ResultThrowVisitor<ISqlExpression> {
    protected final IConfig config;
    protected final SqlExpressionFactory factory;
    protected final Transformer transformer;

    protected BaseSqlVisitor(IConfig config) {
        this.config = config;
        factory = config.getSqlExpressionFactory();
        transformer = config.getTransformer();
    }

    protected void push(ISqlQueryableExpression queryable) {

    }

    protected void pop() {

    }

    protected ISqlExpression mathStaticHandler(MethodCallExpression methodCall) {
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
                return math.pow(arg1, arg2);
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

    protected ISqlExpression stringStaticHandler(MethodCallExpression methodCall) {
        Method method = methodCall.getMethod();
        List<Expression> args = methodCall.getArgs();
        switch (method.getName()) {
            case "join": {
                ISqlExpression delimiter = visit(args.get(0));
                //String.join(CharSequence delimiter, CharSequence... elements)
                if (method.isVarArgs()) {
                    List<ISqlExpression> argList = new ArrayList<>(args.size() - 1);
                    for (int i = 1; i < args.size(); i++) {
                        argList.add(visit(args.get(i)));
                    }
                    return transformer.joinArray(delimiter, argList);
                }
                else {
                    ISqlExpression elements = visit(args.get(1));
                    return transformer.joinList(delimiter, elements);
                }
            }
            case "valueOf": {
                return transformer.typeCast(visit(args.get(0)), String.class);
            }
            default:
                return checkAndReturnValue(methodCall);
        }
    }

    protected ISqlExpression stringHandler(ISqlExpression left, List<Expression> args, Method method, MethodCallExpression methodCall) {
        IStringMethods str = config.getTransformer();
        switch (method.getName()) {
            case "contains": {
                ISqlExpression right = visit(args.get(0));
                return str.contains(left, right);
            }
            case "startsWith": {
                ISqlExpression right = visit(args.get(0));
                return str.startsWith(left, right);
            }
            case "endsWith": {
                ISqlExpression right = visit(args.get(0));
                return str.endsWith(left, right);
            }
            case "length": {
                return str.length(left);
            }
            case "toUpperCase": {
                return str.toUpperCase(left);
            }
            case "toLowerCase": {
                return str.toLowerCase(left);
            }
            case "concat": {
                ISqlExpression right = visit(args.get(0));
                return str.concat(left, right);
            }
            case "trim": {
                return str.trim(left);
            }
            case "isEmpty": {
                return str.isEmpty(left);
            }
            case "indexOf": {
                if (method.getParameterTypes()[0] == String.class) {
                    ISqlExpression subStr = visit(args.get(0));
                    if (method.getParameterCount() == 1) {
                        return str.indexOf(left, subStr);
                    }
                    else {
                        ISqlExpression fromIndex = visit(args.get(1));
                        return str.indexOf(left, subStr, fromIndex);
                    }
                }
            }
            case "replace": {
                ISqlExpression oldStr = visit(args.get(0));
                ISqlExpression newStr = visit(args.get(1));
                return str.replace(left, oldStr, newStr);
            }
            case "substring": {
                ISqlExpression beginIndex = visit(args.get(0));
                if (method.getParameterCount() == 1) {
                    return str.substring(left, beginIndex);
                }
                else {
                    ISqlExpression endIndex = visit(args.get(1));
                    return str.substring(left, beginIndex, endIndex);
                }
            }
            default:
                return checkAndReturnValue(methodCall);
        }
    }

    protected ISqlExpression bigNumberStaticHandler(MethodCallExpression methodCall) {
        Method method = methodCall.getMethod();
        String name = method.getName();
        switch (name) {
            case "valueOf": {
                return transformer.typeCast(visit(methodCall.getArgs().get(0)), method.getReturnType());
            }
            default:
                return checkAndReturnValue(methodCall);
        }
    }

    protected ISqlExpression bigNumberHandler(ISqlExpression left, List<Expression> args, Method method, MethodCallExpression methodCall) {
        switch (method.getName()) {
            case "add": {
                if (method.getParameterCount() == 1) {
                    ISqlExpression right = visit(args.get(0));
                    return factory.binary(SqlOperator.PLUS, left, right);
                }
            }
            case "subtract": {
                if (method.getParameterCount() == 1) {
                    ISqlExpression right = visit(args.get(0));
                    return factory.binary(SqlOperator.MINUS, left, right);
                }
            }
            case "multiply": {
                if (method.getParameterCount() == 1) {
                    ISqlExpression right = visit(args.get(0));
                    return factory.binary(SqlOperator.MUL, left, right);
                }
            }
            case "divide": {
                if (method.getParameterCount() == 1) {
                    ISqlExpression right = visit(args.get(0));
                    return factory.binary(SqlOperator.DIV, left, right);
                }
            }
            case "remainder": {
                if (method.getParameterCount() == 1) {
                    ISqlExpression right = visit(args.get(0));
                    return transformer.remainder(left, right);
                }
            }
            default:
                return checkAndReturnValue(methodCall);
        }
    }

    protected ISqlExpression dateTimeHandler(ISqlExpression left, List<Expression> args, Method method, MethodCallExpression methodCall) {
        switch (method.getName()) {
            case "isAfter": {
                ISqlExpression right = visit(args.get(0));
                return transformer.isAfter(left, right);
            }
            case "isBefore": {
                ISqlExpression right = visit(args.get(0));
                return transformer.isBefore(left, right);
            }
            case "isEqual": {
                ISqlExpression right = visit(args.get(0));
                return transformer.isEqual(left, right);
            }
            default:
                return checkAndReturnValue(methodCall);
        }
    }

    protected ISqlExpression oldDateTimeHandler(ISqlExpression left, List<Expression> args, Method method, MethodCallExpression methodCall) {
        switch (method.getName()) {
            case "after": {
                ISqlExpression right = visit(args.get(0));
                return transformer.isAfter(left, right);
            }
            case "before": {
                ISqlExpression right = visit(args.get(0));
                return transformer.isBefore(left, right);
            }
            default:
                return checkAndReturnValue(methodCall);
        }
    }

    protected ISqlExpression objectsStaticHandler(MethodCallExpression methodCall) {
        Method method = methodCall.getMethod();
        String name = method.getName();
        List<Expression> args = methodCall.getArgs();
        if (name.equals("equals")) {
            return factory.binary(SqlOperator.EQ, visit(args.get(0)), visit(args.get(1)));
        }
        else if (name.equals("deepEquals")) {
            return factory.binary(SqlOperator.EQ, visit(args.get(0)), visit(args.get(1)));
        }
        else if (name.equals("isNull")) {
            return factory.binary(SqlOperator.IS, visit(args.get(0)), factory.nullValue());
        }
        else if (name.equals("nonNull")) {
            return factory.binary(SqlOperator.IS_NOT, visit(args.get(0)), factory.nullValue());
        }
        else {
            return checkAndReturnValue(methodCall);
        }
    }

    protected ISqlExpression mapHandler(ISqlExpression left, List<Expression> args, Method method, MethodCallExpression methodCall) {
        ISqlSingleValueExpression valueExpression = (ISqlSingleValueExpression) left;
        Map<?, ?> map = (Map<?, ?>) valueExpression.getValue();
        if (method.getName().equals("get")) {
            List<String> strings = new ArrayList<>();
            List<ISqlExpression> argList = new ArrayList<>();
            strings.add("(CASE ");
            argList.add(visit(args.get(0)));
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                strings.add(" WHEN ");
                argList.add(factory.AnyValue(entry.getKey()));
                strings.add(" THEN ");
                argList.add(factory.AnyValue(entry.getValue()));
            }
            strings.add(" ELSE NULL END)");
            return factory.template(strings, argList);
        }
        else if (method.getName().equals("getOrDefault")) {
            List<String> strings = new ArrayList<>();
            List<ISqlExpression> argList = new ArrayList<>();
            strings.add("(CASE ");
            argList.add(visit(args.get(0)));
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                strings.add(" WHEN ");
                argList.add(factory.AnyValue(entry.getKey()));
                strings.add(" THEN ");
                argList.add(factory.AnyValue(entry.getValue()));
            }
            strings.add(" ELSE ");
            argList.add(visit(args.get(1)));
            strings.add(" END)");
            return factory.template(strings, argList);
        }
        else {
            return checkAndReturnValue(methodCall);
        }
    }

    private ISqlExpression select1(ISqlQueryableExpression queryable, List<Expression> args) {
        if (!args.isEmpty()) {
            Expression expression = args.get(0);
            ISqlExpression cond = visit(expression);
            queryable.addWhere(cond);
        }
        queryable.setSelect(factory.select(Collections.singletonList(factory.constString(1)), int.class));
        return queryable;
    }

    protected ISqlExpression queryOrDrinkListHandler(ISqlQueryableExpression queryable, List<Expression> args, Method method, MethodCallExpression methodCall) {
        String name = method.getName();
        if (name.equals("count")) {
            ISqlExpression column = null;
            if (!args.isEmpty()) {
                column = visit(args.get(0));
            }
            IAggregateMethods agg = config.getTransformer();
            ISqlSelectExpression select = queryable.getSelect();
            select.getColumns().clear();
            select.addColumn(agg.count(column));
            select.setSingle(true);
            select.setAgg(true);
            select.setTarget(long.class);
            // 在终结的地方弹出
            pop();
            return queryable;
        }
        else if (name.equals("sum")) {
            ISqlExpression column = visit(args.get(0));
            IAggregateMethods agg = config.getTransformer();
            ISqlSelectExpression select = queryable.getSelect();
            select.getColumns().clear();
            select.addColumn(agg.sum(column));
            select.setSingle(true);
            select.setAgg(true);
            // 在终结的地方弹出
            pop();
            return queryable;
        }
        else if (name.equals("avg")) {
            ISqlExpression column = visit(args.get(0));
            IAggregateMethods agg = config.getTransformer();
            ISqlSelectExpression select = queryable.getSelect();
            select.getColumns().clear();
            select.addColumn(agg.avg(column));
            select.setSingle(true);
            select.setAgg(true);
            // 在终结的地方弹出
            pop();
            return queryable;
        }
        else if (name.equals("min")) {
            ISqlExpression column = visit(args.get(0));
            IAggregateMethods agg = config.getTransformer();
            ISqlSelectExpression select = queryable.getSelect();
            select.getColumns().clear();
            select.addColumn(agg.min(column));
            select.setSingle(true);
            select.setAgg(true);
            // 在终结的地方弹出
            pop();
            return queryable;
        }
        else if (name.equals("max")) {
            ISqlExpression column = visit(args.get(0));
            IAggregateMethods agg = config.getTransformer();
            ISqlSelectExpression select = queryable.getSelect();
            select.getColumns().clear();
            select.addColumn(agg.max(column));
            select.setSingle(true);
            select.setAgg(true);
            // 在终结的地方弹出
            pop();
            return queryable;
        }
        else if (name.equals("any")) {
            ISqlExpression any = factory.unary(SqlOperator.EXISTS, select1(queryable, args));
            // 在终结的地方弹出
            pop();
            return any;
        }
        else if (name.equals("none")) {
            ISqlExpression none = factory.unary(SqlOperator.NOT_EXISTS, select1(queryable, args));
            // 在终结的地方弹出
            pop();
            return none;
        }
        else if (name.equals("where")) {
            ISqlExpression cond = visit(args.get(0));
            queryable.addWhere(cond);
            return queryable;
        }
        else if (name.equals("select")) {
            ISqlExpression select = visit(args.get(0));
            queryable.setSelect(factory.select(Collections.singletonList(select), queryable.getMainTableClass()));
            return factory.queryable(queryable.getSelect(), factory.from(queryable, queryable.getFrom().getTableRefExpression()));
        }
        else if (name.equals("distinct")) {
            if (args.isEmpty()) {
                queryable.setDistinct(true);
            }
            else {
                ISqlExpression visited = visit(args.get(0));
                if (visited instanceof ISqlSingleValueExpression) {
                    ISqlSingleValueExpression iSqlSingleValueExpression = (ISqlSingleValueExpression) visited;
                    queryable.setDistinct((boolean) iSqlSingleValueExpression.getValue());
                }
                else {
                    throw new SqLinkException("不支持的表达式:" + methodCall);
                }
            }
            return queryable;
        }
        else if (name.equals("orderBy")) {
            ISqlExpression orderByColumn = visit(args.get(0));

            if (args.size() > 1) {
                ISqlExpression visited = visit(args.get(1));
                if (visited instanceof ISqlSingleValueExpression) {
                    ISqlSingleValueExpression iSqlSingleValueExpression = (ISqlSingleValueExpression) visited;
                    queryable.addOrder(factory.order(orderByColumn, (boolean) iSqlSingleValueExpression.getValue()));
                }
                else {
                    throw new SqLinkException("不支持的表达式:" + methodCall);
                }
            }
            else {
                queryable.addOrder(factory.order(orderByColumn));
            }
            return queryable;
        }
        else if (name.equals("leftJoin")) {
            throw new SqLinkException("过于复杂的表达式:" + methodCall);
        }
        else if (name.equals("innerJoin")) {
            throw new SqLinkException("过于复杂的表达式:" + methodCall);
        }
        else if (name.equals("rightJoin")) {
            throw new SqLinkException("过于复杂的表达式:" + methodCall);
        }
        else if (name.equals("groupBy")) {
            throw new SqLinkException("过于复杂的表达式:" + methodCall);
        }
        else if (name.equals("having")) {
            throw new SqLinkException("过于复杂的表达式:" + methodCall);
        }
        else if (name.equals("limit")) {
            if (args.size() == 1) {
                ISqlExpression rows = visit(args.get(0));
                if (rows instanceof ISqlSingleValueExpression) {
                    ISqlSingleValueExpression iSqlSingleValueExpression = (ISqlSingleValueExpression) rows;
                    queryable.setLimit(0, (long) iSqlSingleValueExpression.getValue());
                }
                else {
                    throw new SqLinkException("不支持的表达式:" + methodCall);
                }
            }
            else if (args.size() == 2) {
                ISqlExpression offset = visit(args.get(0));
                ISqlExpression rows = visit(args.get(1));
                if (rows instanceof ISqlSingleValueExpression && offset instanceof ISqlSingleValueExpression) {
                    ISqlSingleValueExpression rowsValue = (ISqlSingleValueExpression) rows;
                    ISqlSingleValueExpression offsetValue = (ISqlSingleValueExpression) offset;
                    queryable.setLimit((long) offsetValue.getValue(), (long) rowsValue.getValue());
                }
                else {
                    throw new SqLinkException("不支持的表达式:" + methodCall);
                }
            }
            return queryable;
        }
        else if (name.equals("toList")) {
            // 在终结的地方弹出
            pop();
            return queryable;
        }
        else if (name.equals("first")) {
            queryable.setLimit(0, 1);
            // 在终结的地方弹出
            pop();
            return queryable;
        }
        else if (name.equals("get")) {
            Expression expression = args.get(0);
            int value = (int) expression.getValue();
            queryable.setLimit(value, value + 1);
            // 在终结的地方弹出
            pop();
            return queryable;
        }
        else {
            return checkAndReturnValue(methodCall);
        }
    }

    protected ISqlExpression collectionHandler(ISqlExpression left, List<Expression> args, Method method, MethodCallExpression methodCall) {
        String name = method.getName();
        if (name.equals("contains")) {
            ISqlExpression right = visit(args.get(0));
            return factory.binary(SqlOperator.IN, right, left);
        }
        else if (name.equals("size")) {
            if (left instanceof ISqlQueryableExpression) {
                ISqlQueryableExpression query = (ISqlQueryableExpression) left;
                IAggregateMethods agg = config.getTransformer();
                query.setSelect(factory.select(Collections.singletonList(agg.count()), long.class));
                // 在终结的地方弹出
                pop();
                return query;
            }
            else {
                throw new DrinkException(String.format("意外的sql表达式类型:%s 表达式为:%s", left.getClass(), methodCall));
            }
        }
        else if (name.equals("isEmpty")) {
            if (left instanceof ISqlQueryableExpression) {
                ISqlQueryableExpression query = (ISqlQueryableExpression) left;
                query.setSelect(factory.select(Collections.singletonList(factory.constString("1")), int.class));
                ISqlUnaryExpression not = factory.unary(SqlOperator.NOT, factory.unary(SqlOperator.EXISTS, query));
                // 在终结的地方弹出
                pop();
                return not;
            }
            else {
                throw new SqLinkException(String.format("意外的sql表达式类型:%s 表达式为:%s", left.getClass(), methodCall));
            }
        }
        else if (name.equals("get")) {
            if (left instanceof ISqlJsonObject) {
                ISqlJsonObject jsonObject = (ISqlJsonObject) left;
                Expression expression = args.get(0);
                Integer index = (Integer) expression.getValue();
                List<JsonProperty> jsonPropertyList = jsonObject.getJsonPropertyList();
                if (jsonPropertyList.isEmpty()) {
                    jsonObject.setIndex(index);
                }
                else {
                    last(jsonPropertyList).setIndex(index);
                }
                return left;
            }
            else if (left instanceof ISqlQueryableExpression) {
                ISqlQueryableExpression queryableExpression = (ISqlQueryableExpression) left;
                Expression expression = args.get(0);
                int index = (int) expression.getValue();
                queryableExpression.setLimit(index, index + 1);
                // 在终结的地方弹出
                pop();
                return queryableExpression;
            }
            else {
                throw new SqLinkException(String.format("意外的sql表达式类型:%s 表达式为:%s", left.getClass(), methodCall));
            }
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

        private final Class<?> type;

        public JavaType(Class<?> type) {
            this.type = type;
        }

        @Override
        public String getSqlAndValue(IConfig config, List<SqlValue> values) {
            return "";
        }

        @Override
        public <T extends ISqlExpression> T copy(IConfig config) {
            return null;
        }

        public Class<?> getType() {
            return type;
        }
    }
}
