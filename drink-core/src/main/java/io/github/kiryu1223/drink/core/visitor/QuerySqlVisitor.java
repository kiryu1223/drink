package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.IMappingTable;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.metaData.NavigateData;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.sqlExt.BaseSqlExtension;
import io.github.kiryu1223.drink.base.sqlExt.SqlExtensionExpression;
import io.github.kiryu1223.drink.base.sqlExt.SqlOperatorMethod;
import io.github.kiryu1223.drink.base.transform.IAggregateMethods;
import io.github.kiryu1223.drink.base.util.DrinkUtil;
import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.drink.core.api.crud.read.Aggregate;
import io.github.kiryu1223.drink.core.api.crud.read.QueryBase;
import io.github.kiryu1223.drink.core.api.crud.read.group.Grouper;
import io.github.kiryu1223.drink.core.api.crud.read.group.IGroup;
import io.github.kiryu1223.drink.core.exception.SqLinkException;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.last;
import static io.github.kiryu1223.drink.core.util.ExpressionUtil.*;

public class QuerySqlVisitor extends BaseSqlVisitor {
    protected int index;
    protected final List<List<ISqlTableRefExpression>> tableRefListList = new ArrayList<>();
    protected final Map<ParameterExpression, ISqlTableRefExpression> asNameMap = new HashMap<>();
    protected final List<ISqlQueryableExpression> queryableList = new ArrayList<>();

    public QuerySqlVisitor(IConfig config, ISqlQueryableExpression sqlQueryableExpression) {
        this(config, sqlQueryableExpression, -1);
    }

    public QuerySqlVisitor(IConfig config, ISqlQueryableExpression sqlQueryableExpression, int index) {
        super(config);
        this.index = index;
        push(sqlQueryableExpression);
    }

    protected void push(ISqlQueryableExpression queryable) {
        List<ISqlTableRefExpression> tableRefs = getTableRefs(queryable);
        tableRefListList.add(tableRefs);
        queryableList.add(queryable);
    }

    protected void pop() {
        tableRefListList.remove(tableRefListList.size() - 1);
        queryableList.remove(queryableList.size() - 1);
    }

    /**
     * lambda表达式解析
     */
    @Override
    public ISqlExpression visit(LambdaExpression<?> lambda) {
//        List<ParameterExpression> parameters = lambda.getParameters();
//        // 是否第一次进入或Group的聚合函数查询
//        if (isFirst || isGroup) {
//            isFirst = false;
//            for (int i = 0; i < parameters.size(); i++) {
//                ParameterExpression parameter = parameters.get(i);
//                ISqlTableRefExpression asName;
//                if (i == 0) {
//                    asName = fromExpression.getISqlTableRefExpression();
//                }
//                else {
//                    asName = joinsExpression.getJoins().get(i - 1).getISqlTableRefExpression();
//                }
//                asNameMap.put(parameter, asName);
//            }
//            ISqlExpression visit = visit(lambda.getBody());
//            for (ParameterExpression parameter : parameters) {
//                asNameMap.remove(parameter);
//            }
//            return visit;
//        }
//        // 不是的话说明有子查询
//        else {
//            for (ParameterExpression parameter : parameters) {
//                asNameMap.put(parameter, asNameDeque.peek());
//            }
//            ISqlExpression visit = visit(lambda.getBody());
//            for (ParameterExpression parameter : parameters) {
//                asNameMap.remove(parameter);
//            }
//            return visit;
//        }
        List<ParameterExpression> parameters = lambda.getParameters();
        // g -> ...
        if (parameters.size() == 1 && IGroup.class.isAssignableFrom(parameters.get(0).getType())) {
            ParameterExpression parameterExpression = parameters.get(0);
            return visit(lambda.getBody());
        }
        // (a,b,c) -> ...
        else {
            List<ISqlTableRefExpression> last = last(tableRefListList);
            for (int i = 0; i < parameters.size(); i++) {
                asNameMap.put(parameters.get(i), last.get(i));
            }
            ISqlExpression visit = visit(lambda.getBody());
            for (ParameterExpression parameter : parameters) {
                asNameMap.remove(parameter);
            }
            return visit;
        }
    }

//    protected String doGetISqlTableRefExpression(String as) {
//        return doGetISqlTableRefExpression(as, 0);
//    }
//
//    protected String doGetISqlTableRefExpression(String as, int offset) {
//        String next = offset == 0 ? as : as + offset;
//        if (asNameSet.contains(next)) {
//            return doGetISqlTableRefExpression(as, offset + 1);
//        }
//        else {
//            asNameSet.add(next);
//            return next;
//        }
//    }

    /**
     * 赋值表达式解析
     */
    @Override
    public ISqlExpression visit(AssignExpression assignExpression) {
        ISqlExpression left = visit(assignExpression.getLeft());
        if (left instanceof ISqlColumnExpression) {
            ISqlColumnExpression sqlColumnExpression = (ISqlColumnExpression) left;
            ISqlExpression right = visit(assignExpression.getRight());
            return factory.set(sqlColumnExpression, right);
        }
        throw new SqLinkException("表达式中不能出现非lambda入参为赋值对象的语句");
    }

    /**
     * 字段访问表达式解析
     */
    @Override
    public ISqlExpression visit(FieldSelectExpression fieldSelect) {
        ISqlExpression left = visit(fieldSelect.getExpr());
        Field field = fieldSelect.getField();
        String fieldName = field.getName();
        // g
        if (left instanceof SqlGroupRef) {
            // g.key
            if (fieldName.equals("key")) {
                return last(queryableList).getGroupBy();
            }
            // g.value?
            else if (fieldName.startsWith("value")) {
                int index = Integer.parseInt(fieldName.replace("value", ""));
                List<ISqlTableRefExpression> list = last(tableRefListList);
                return list.get(index - 1);
            }
            else {
                throw new DrinkException(fieldSelect.toString());
            }
        }
        else if (left instanceof ISqlGroupByExpression) {
            ISqlGroupByExpression group = (ISqlGroupByExpression) left;
            return group.getColumns().get(fieldName);
        }
        else if (left instanceof ISqlTableRefExpression) {
            if (Aggregate.class.isAssignableFrom(field.getDeclaringClass())) {
                return left;
            }
            else {
                ISqlTableRefExpression tableRef = (ISqlTableRefExpression) left;
                MetaData metaData = config.getMetaData(field.getDeclaringClass());
                FieldMetaData fieldMetaData = metaData.getFieldMetaDataByFieldName(field.getName());
                if (fieldMetaData.hasNavigate()) {
                    return getterToSqlAst(fieldMetaData, tableRef);
                }
                else {
                    return factory.column(fieldMetaData, tableRef);
                }
            }
        }
        else {
            return checkAndReturnValue(fieldSelect);
        }
    }


//    @Override
//    public ISqlExpression visit(MethodCallExpression methodCall) {
//        // equals
//        if (isEquals(methodCall)) {
//            return factory.binary(SqlOperator.EQ, visit(methodCall.getExpr()), visit(methodCall.getArgs().get(0)));
//        }
//        // SQL扩展函数
//        else if (isSqlExtensionExpressionMethod(methodCall.getMethod())) {
//            Method sqlFunction = methodCall.getMethod();
//            SqlExtensionExpression sqlFuncExt = getSqlFuncExt(sqlFunction.getAnnotationsByType(SqlExtensionExpression.class));
//            List<Expression> args = methodCall.getArgs();
//            List<ISqlExpression> expressions = new ArrayList<>(args.size());
//            if (sqlFuncExt.extension() != BaseSqlExtension.class) {
//                for (Expression arg : args) {
//                    expressions.add(visit(arg));
//                }
//                BaseSqlExtension sqlExtension = BaseSqlExtension.getCache(sqlFuncExt.extension());
//                boolean[] useSuper = {false};
//                ISqlExpression parse = sqlExtension.parse(config, sqlFunction, expressions, useSuper);
//                if (useSuper[0]) {
//                    parse = sqlExtension.callSuper(visit(methodCall.getExpr()), parse);
//                }
//                return parse;
//            }
//            else {
//                List<String> strings = new ArrayList<>();
//                List<Parameter> methodParameters = Arrays.stream(methodCall.getMethod().getParameters()).collect(Collectors.toList());
//                ParamMatcher match = match(sqlFuncExt.template());
//                List<String> functions = match.remainder;
//                List<String> params = match.bracesContent;
//                for (int i = 0; i < functions.size(); i++) {
//                    strings.add(functions.get(i));
//                    if (i < params.size()) {
//                        String param = params.get(i);
//                        // this应该获取前一步的结果
//                        if (param.equals("super")) {
//                            expressions.add(visit(methodCall.getExpr()));
//                        }
//                        else {
//                            boolean isAnd = param.startsWith("&");
//                            String paramFinal = isAnd ? param.substring(1) : param;
//                            Parameter targetParam = methodParameters.stream()
//                                    .filter(f -> f.getName().equals(paramFinal))
//                                    .findFirst()
//                                    .orElseThrow(() -> new DrinkException(String.format("无法在%s中找到%s", sqlFuncExt.template(), paramFinal)));
//                            int index = methodParameters.indexOf(targetParam);
//
//                            // 如果是可变参数
//                            boolean varArgs = targetParam.isVarArgs();
//                            if (varArgs) {
//                                while (index < args.size()) {
//                                    if (isAnd) {
//                                        parse$(args.get(index), strings);
//                                    }
//                                    else {
//                                        expressions.add(visit(args.get(index)));
//                                        if (index < args.size() - 1) {
//                                            strings.add(sqlFuncExt.separator());
//                                        }
//                                        index++;
//                                    }
//                                }
//                            }
//                            // 正常情况
//                            else {
//                                if (isAnd) {
//                                    parse$(args.get(index), strings);
//                                }
//                                else {
//                                    expressions.add(visit(args.get(index)));
//                                }
//                            }
//                        }
//                    }
//                }
//                return factory.template(strings, expressions);
//            }
//        }
//        // SQL运算符
//        else if (isSqlOperatorMethod(methodCall.getMethod())) {
//            Method method = methodCall.getMethod();
//            List<Expression> args = methodCall.getArgs();
//            SqlOperatorMethod operatorMethod = method.getAnnotation(SqlOperatorMethod.class);
//            SqlOperator operator = operatorMethod.value();
//            if (operator == SqlOperator.BETWEEN) {
//                ISqlExpression thiz = visit(args.get(0));
//                ISqlExpression min = visit(args.get(1));
//                ISqlExpression max = visit(args.get(2));
//                return factory.binary(SqlOperator.BETWEEN, thiz, factory.binary(SqlOperator.AND, min, max));
//            }
//            else {
//                if (operator.isLeft() || operator == SqlOperator.POSTINC || operator == SqlOperator.POSTDEC) {
//                    ISqlExpression visit = visit(args.get(0));
//                    if (visit instanceof ISqlQueryableExpression) {
//                        visit = factory.unary(operator, visit);
//                    }
//                    return factory.unary(operator, visit);
//                }
//                else {
//                    ISqlExpression left = visit(methodCall.getArgs().get(0));
//                    ISqlExpression right = visit(methodCall.getArgs().get(1));
//                    if (left instanceof ISqlQueryableExpression) {
//                        left = factory.parens(left);
//                    }
//                    if (right instanceof ISqlQueryableExpression) {
//                        right = factory.parens(right);
//                    }
//                    return factory.binary(operator, left, right);
//                }
//            }
//        }
//        // 子查询发起者
//        else if (isStartQuery(methodCall.getMethod())) {
//            Expression expression = methodCall.getArgs().get(0);
//            return visit(expression);
//        }
//        else if (SqlClient.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass())) {
//            Method method = methodCall.getMethod();
//            String methodName = method.getName();
//            Expression expression = methodCall.getArgs().get(0);
//            if (methodName.equals("query") && expression.getKind() == Kind.StaticClass) {
//                StaticClassExpression staticClass = (StaticClassExpression) expression;
//                ISqlQueryableExpression queryable = factory.queryable(staticClass.getType());
//                push(queryable);
//                return queryable;
//            }
//            return checkAndReturnValue(methodCall);
//        }
//        // 子查询
//        else if (QueryBase.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass())
//                 || isList(methodCall.getMethod())) {
//            Method method = methodCall.getMethod();
//            String methodName = method.getName();
//            if (methodName.equals("count")) {
//                ISqlExpression visit = visit(methodCall.getExpr());
//                if (!(visit instanceof ISqlQueryableExpression)) {
//                    throw new SqLinkException("不支持的表达式:" + methodCall);
//                }
//                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
//                ISqlExpression column = null;
//                if (!methodCall.getArgs().isEmpty()) {
//                    column = visit(methodCall.getArgs().get(0));
//                }
//                IAggregateMethods agg = config.getTransformer();
//                ISqlSelectExpression select = queryable.getSelect();
//                select.getColumns().clear();
//                select.addColumn(agg.count(column));
//                select.setSingle(true);
//                select.setAgg(true);
//                select.setTarget(long.class);
//                // 在终结的地方弹出
//                pop();
//                return queryable;
//            }
//            else if (methodName.equals("sum")) {
//                ISqlExpression visit = visit(methodCall.getExpr());
//                if (!(visit instanceof ISqlQueryableExpression)) {
//                    throw new SqLinkException("不支持的表达式:" + methodCall);
//                }
//                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
//                ISqlExpression column = visit(methodCall.getArgs().get(0));
//                IAggregateMethods agg = config.getTransformer();
//                ISqlSelectExpression select = queryable.getSelect();
//                select.getColumns().clear();
//                select.addColumn(agg.sum(column));
//                select.setSingle(true);
//                select.setAgg(true);
//                // 在终结的地方弹出
//                pop();
//                return queryable;
//            }
//            else if (methodName.equals("avg")) {
//                ISqlExpression visit = visit(methodCall.getExpr());
//                if (!(visit instanceof ISqlQueryableExpression)) {
//                    throw new SqLinkException("不支持的表达式:" + methodCall);
//                }
//                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
//                ISqlExpression column = visit(methodCall.getArgs().get(0));
//                IAggregateMethods agg = config.getTransformer();
//                ISqlSelectExpression select = queryable.getSelect();
//                select.getColumns().clear();
//                select.addColumn(agg.avg(column));
//                select.setSingle(true);
//                select.setAgg(true);
//                // 在终结的地方弹出
//                pop();
//                return queryable;
//            }
//            else if (methodName.equals("min")) {
//                ISqlExpression visit = visit(methodCall.getExpr());
//                if (!(visit instanceof ISqlQueryableExpression)) {
//                    throw new SqLinkException("不支持的表达式:" + methodCall);
//                }
//                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
//                ISqlExpression column = visit(methodCall.getArgs().get(0));
//                IAggregateMethods agg = config.getTransformer();
//                ISqlSelectExpression select = queryable.getSelect();
//                select.getColumns().clear();
//                select.addColumn(agg.min(column));
//                select.setSingle(true);
//                select.setAgg(true);
//                // 在终结的地方弹出
//                pop();
//                return queryable;
//            }
//            else if (methodName.equals("max")) {
//                ISqlExpression visit = visit(methodCall.getExpr());
//                if (!(visit instanceof ISqlQueryableExpression)) {
//                    throw new SqLinkException("不支持的表达式:" + methodCall);
//                }
//                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
//                ISqlExpression column = visit(methodCall.getArgs().get(0));
//                IAggregateMethods agg = config.getTransformer();
//                ISqlSelectExpression select = queryable.getSelect();
//                select.getColumns().clear();
//                select.addColumn(agg.max(column));
//                select.setSingle(true);
//                select.setAgg(true);
//                // 在终结的地方弹出
//                pop();
//                return queryable;
//            }
//            else if (methodName.equals("any")) {
//                ISqlExpression visit = visit(methodCall.getExpr());
//                if (!(visit instanceof ISqlQueryableExpression)) {
//                    throw new SqLinkException("不支持的表达式:" + methodCall);
//                }
//                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
//                List<Expression> args = methodCall.getArgs();
//                if (!args.isEmpty()) {
//                    Expression expression = args.get(0);
//                    ISqlExpression cond = visit(expression);
//                    queryable.addWhere(cond);
//                }
//                queryable.setSelect(factory.select(Collections.singletonList(factory.constString(1)), int.class));
//                ISqlUnaryExpression any = factory.unary(SqlOperator.EXISTS, queryable);
//                // 在终结的地方弹出
//                pop();
//                return any;
//            }
//            else if (methodName.equals("where")) {
//                ISqlExpression visit = visit(methodCall.getExpr());
//                if (!(visit instanceof ISqlQueryableExpression)) {
//                    throw new SqLinkException("不支持的表达式:" + methodCall);
//                }
//                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
//                ISqlExpression cond = visit(methodCall.getArgs().get(0));
//                queryable.addWhere(cond);
//                return queryable;
//            }
//            else if (methodName.equals("select")) {
//                ISqlExpression visit = visit(methodCall.getExpr());
//                if (!(visit instanceof ISqlQueryableExpression)) {
//                    throw new SqLinkException("不支持的表达式:" + methodCall);
//                }
//                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
//                ISqlExpression select = visit(methodCall.getArgs().get(0));
//                queryable.setSelect(factory.select(Collections.singletonList(select), queryable.getMainTableClass()));
//                return factory.queryable(queryable.getSelect(), factory.from(queryable, queryable.getFrom().getTableRefExpression()));
//            }
//            else if (methodName.equals("distinct")) {
//
//                ISqlExpression visit = visit(methodCall.getExpr());
//                if (!(visit instanceof ISqlQueryableExpression)) {
//                    throw new SqLinkException("不支持的表达式:" + methodCall);
//                }
//                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
//                List<Expression> args = methodCall.getArgs();
//                if (args.isEmpty()) {
//                    queryable.setDistinct(true);
//                }
//                else {
//                    ISqlExpression value = visit(args.get(0));
//                    if (value instanceof ISqlSingleValueExpression) {
//                        ISqlSingleValueExpression iSqlSingleValueExpression = (ISqlSingleValueExpression) value;
//                        queryable.setDistinct((boolean) iSqlSingleValueExpression.getValue());
//                    }
//                    else {
//                        throw new SqLinkException("不支持的表达式:" + methodCall);
//                    }
//                }
//                return queryable;
//            }
//            else if (methodName.equals("orderBy")) {
//
//                ISqlExpression visit = visit(methodCall.getExpr());
//                if (!(visit instanceof ISqlQueryableExpression)) {
//                    throw new SqLinkException("不支持的表达式:" + methodCall);
//                }
//                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
//                List<Expression> args = methodCall.getArgs();
//                ISqlExpression orderByColumn = visit(args.get(0));
//
//                if (args.size() > 1) {
//                    ISqlExpression value = visit(args.get(1));
//                    if (value instanceof ISqlSingleValueExpression) {
//                        ISqlSingleValueExpression iSqlSingleValueExpression = (ISqlSingleValueExpression) value;
//                        queryable.addOrder(factory.order(orderByColumn, (boolean) iSqlSingleValueExpression.getValue()));
//                    }
//                    else {
//                        throw new SqLinkException("不支持的表达式:" + methodCall);
//                    }
//                }
//                else {
//                    queryable.addOrder(factory.order(orderByColumn));
//                }
//                return queryable;
//            }
//            else if (methodName.equals("leftJoin")) {
//                throw new SqLinkException("过于复杂的表达式:" + methodCall);
//            }
//            else if (methodName.equals("innerJoin")) {
//                throw new SqLinkException("过于复杂的表达式:" + methodCall);
//            }
//            else if (methodName.equals("rightJoin")) {
//                throw new SqLinkException("过于复杂的表达式:" + methodCall);
//            }
//            else if (methodName.equals("groupBy")) {
//                throw new SqLinkException("过于复杂的表达式:" + methodCall);
//            }
//            else if (methodName.equals("having")) {
//                throw new SqLinkException("过于复杂的表达式:" + methodCall);
//            }
//            else if (methodName.equals("limit")) {
//
//                ISqlExpression visit = visit(methodCall.getExpr());
//                if (!(visit instanceof ISqlQueryableExpression)) {
//                    throw new SqLinkException("不支持的表达式:" + methodCall);
//                }
//                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
//
//                List<Expression> args = methodCall.getArgs();
//                if (args.size() == 1) {
//                    ISqlExpression rows = visit(args.get(0));
//                    if (rows instanceof ISqlSingleValueExpression) {
//                        ISqlSingleValueExpression iSqlSingleValueExpression = (ISqlSingleValueExpression) rows;
//                        queryable.setLimit(0, (long) iSqlSingleValueExpression.getValue());
//                    }
//                    else {
//                        throw new SqLinkException("不支持的表达式:" + methodCall);
//                    }
//                }
//                else if (args.size() == 2) {
//                    ISqlExpression offset = visit(args.get(0));
//                    ISqlExpression rows = visit(args.get(1));
//                    if (rows instanceof ISqlSingleValueExpression && offset instanceof ISqlSingleValueExpression) {
//                        ISqlSingleValueExpression rowsValue = (ISqlSingleValueExpression) rows;
//                        ISqlSingleValueExpression offsetValue = (ISqlSingleValueExpression) offset;
//                        queryable.setLimit((long) offsetValue.getValue(), (long) rowsValue.getValue());
//                    }
//                    else {
//                        throw new SqLinkException("不支持的表达式:" + methodCall);
//                    }
//                }
//                return queryable;
//            }
//            else if (methodName.equals("toList")) {
//                ISqlExpression visit = visit(methodCall.getExpr());
//                if (!(visit instanceof ISqlQueryableExpression)) {
//                    throw new SqLinkException("不支持的表达式:" + methodCall);
//                }
//                // 在终结的地方弹出
//                pop();
//                return visit;
//            }
//            else {
//                return checkAndReturnValue(methodCall);
//            }
//        }
//        // 集合的函数
//        else if (Collection.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass())) {
//            Method method = methodCall.getMethod();
//            if (method.getName().equals("contains")) {
//                ISqlExpression left = visit(methodCall.getArgs().get(0));
//                ISqlExpression right = visit(methodCall.getExpr());
//                ISqlBinaryExpression binary = factory.binary(SqlOperator.IN, left, right);
//                // 在终结的地方弹出
//                pop();
//                return binary;
//            }
//            else if (method.getName().equals("size")) {
//                ISqlExpression left = visit(methodCall.getExpr());
//                if (left instanceof ISqlQueryableExpression) {
//                    ISqlQueryableExpression query = (ISqlQueryableExpression) left;
//                    IAggregateMethods agg = config.getTransformer();
//                    query.setSelect(factory.select(Collections.singletonList(agg.count()), long.class));
//                    // 在终结的地方弹出
//                    pop();
//                    return query;
//                }
//                else {
//                    throw new DrinkException(String.format("意外的sql表达式类型:%s 表达式为:%s", left.getClass(), methodCall));
//                }
//            }
//            else if (method.getName().equals("isEmpty")) {
//                ISqlExpression left = visit(methodCall.getExpr());
//                if (left instanceof ISqlQueryableExpression) {
//                    ISqlQueryableExpression query = (ISqlQueryableExpression) left;
//                    query.setSelect(factory.select(Collections.singletonList(factory.constString("1")), int.class));
//                    ISqlUnaryExpression not = factory.unary(SqlOperator.NOT, factory.unary(SqlOperator.EXISTS, query));
//                    // 在终结的地方弹出
//                    pop();
//                    return not;
//                }
//                else {
//                    throw new SqLinkException(String.format("意外的sql表达式类型:%s 表达式为:%s", left.getClass(), methodCall));
//                }
//            }
//            else if (method.getName().equals("get")) {
//                ISqlExpression left = visit(methodCall.getExpr());
//                if (left instanceof ISqlJsonObject) {
//                    ISqlJsonObject jsonObject = (ISqlJsonObject) left;
//                    Expression expression = methodCall.getArgs().get(0);
//                    Integer index = (Integer) expression.getValue();
//                    List<JsonProperty> jsonPropertyList = jsonObject.getJsonPropertyList();
//                    if (jsonPropertyList.isEmpty()) {
//                        jsonObject.setIndex(index);
//                    }
//                    else {
//                        last(jsonPropertyList).setIndex(index);
//                    }
//                    return left;
//                }
//                else {
//                    throw new SqLinkException(String.format("意外的sql表达式类型:%s 表达式为:%s", left.getClass(), methodCall));
//                }
//            }
//            else {
//                return checkAndReturnValue(methodCall);
//            }
//        }
//        else if (Map.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass())) {
//            return mapHandler(methodCall);
//        }
//        // 字符串的函数
//        else if (String.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass())) {
//            return stringHandler(methodCall);
//        }
//        // Math的函数
//        else if (Math.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass())) {
//            return mathStaticHandler(methodCall);
//        }
//        // BigDecimal||BigInteger的函数
//        else if (BigDecimal.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass())
//                 || BigInteger.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass())) {
//            return bigNumberHandler(methodCall);
//        }
//        // 时间的函数
//        else if (Temporal.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass())) {
//            return dateTimeHandler(methodCall);
//        }
//        // Objects的函数
//        else if (Objects.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass())) {
//            return objectsStaticHandler(methodCall);
//        }
//        else {
//            Expression expression = methodCall.getExpr();
//            Method method = methodCall.getMethod();
//            ISqlExpression left = visit(expression);
//            // (a) -> a
//            if (left instanceof ISqlTableRefExpression) {
//                ISqlTableRefExpression tableRef = (ISqlTableRefExpression) left;
//                // a.getter()
//                if (isGetter(method)) {
//                    FieldMetaData getter = methodToFieldMetaData(method, tableRef);
//                    // 如果这个getter是导航属性
//                    // 那么他应该被映射成sql语句
//                    if (getter.hasNavigate()) {
//                        return getterToSqlAst(getter, tableRef);
//                    }
//                    else {
//                        ISqlColumnExpression column = factory.column(getter, tableRef);
//                        if (getter.isJsonObject()) {
//                            return factory.jsonProperty(column);
//                        }
//                        else {
//                            return column;
//                        }
//                    }
//                }
//                else if (isPivoted(method)) {
//                    Expression args = methodCall.getArgs().get(0);
//                    ISqlSingleValueExpression singleValue = (ISqlSingleValueExpression) visit(args);
//                    Object value = singleValue.getValue();
//                    Class<?> aggregationType = ((ISqlPivotExpression) last(queryableList).getFrom().getSqlTableExpression()).getAggregationType();
//                    return factory.dynamicColumn(value.toString(), aggregationType, tableRef);
//                }
//                else if (isUnPivotedName(method)) {
//                    ISqlUnPivotExpression unPivot = (ISqlUnPivotExpression) last(queryableList).getFrom().getSqlTableExpression();
//                    return factory.dynamicColumn(unPivot.getNewNameColumnName(), String.class, tableRef);
//                }
//                else if (isUnPivotedValue(method)) {
//                    ISqlUnPivotExpression unPivot = (ISqlUnPivotExpression) last(queryableList).getFrom().getSqlTableExpression();
//                    return factory.dynamicColumn(unPivot.getNewValueColumnName(), unPivot.getNewValueColumnType(), tableRef);
//                }
//                // else if (isDynamicColumn(method)) {
//                //     List<Expression> args = methodCall.getArgs();
//                //     String column = args.get(0).getValue().toString();
//                //     Class<?> type = (Class<?>) args.get(1).getValue();
//                //     return factory.dynamicColumn(column, type, tableRef);
//                // }
//                else {
//                    return checkAndReturnValue(methodCall);
//                }
//            }
//            // (a) -> a.jsonObjectField
//            else if (left instanceof ISqlJsonObject) {
//                ISqlJsonObject jsonObject = (ISqlJsonObject) left;
//                MetaData metaData = config.getMetaData(method.getDeclaringClass());
//                FieldMetaData getter = metaData.getFieldMetaDataByGetter(method);
//                jsonObject.addJsonProperty(new JsonProperty(getter));
//                return jsonObject;
//            }
//            // ?.table()
//            else if (left instanceof ISqlQueryableExpression) {
//                ISqlQueryableExpression leftQuery = (ISqlQueryableExpression) left;
//                // ?.table().getter()
//                if (isGetter(method)) {
//                    MetaData metaData = config.getMetaData(method.getDeclaringClass());
//                    FieldMetaData getter = metaData.getFieldMetaDataByGetter(method);
//                    if (getter.hasNavigate()) {
//                        ISqlQueryableExpression query = queryToQueryable(leftQuery, getter);
//                        // 弹出旧的
//                        pop();
//                        //  在子查询发起的地方压入
//                        push(query);
//                        return query;
//                    }
//                    else {
//                        // select ? from table ...
//                        // select table.getter from table ...
//                        leftQuery.setSelect(factory.select(Collections.singletonList(factory.column(getter, leftQuery.getFrom().getTableRefExpression())), getter.getType()));
//                        return leftQuery;
//                    }
//                }
//                else {
//                    throw new DrinkException(methodCall.toString());
//                }
//            }
//            else {
//                return checkAndReturnValue(methodCall);
//            }
//        }
//    }

    /**
     * 方法调用表达式解析
     */
    public ISqlExpression visit(MethodCallExpression methodCall) {
        Method method = methodCall.getMethod();
        List<Expression> args = methodCall.getArgs();
        ISqlExpression left = visit(methodCall.getExpr());
        // (a) -> a
        if (left instanceof ISqlTableRefExpression) {
            ISqlTableRefExpression tableRef = (ISqlTableRefExpression) left;
            // a.getter()
            if (isGetter(method)) {
                FieldMetaData getter = methodToFieldMetaData(method, tableRef);
                // 如果这个getter是导航属性
                // 那么他应该被映射成sql语句
                if (getter.hasNavigate()) {
                    return getterToSqlAst(getter, tableRef);
                }
                else {
                    ISqlColumnExpression column = factory.column(getter, tableRef);
                    if (getter.isJsonObject()) {
                        return factory.jsonProperty(column);
                    }
                    else {
                        return column;
                    }
                }
            }
            else if (isPivoted(method)) {
                Expression arg = args.get(0);
                ISqlSingleValueExpression singleValue = (ISqlSingleValueExpression) visit(arg);
                Object value = singleValue.getValue();
                Class<?> aggregationType = ((ISqlPivotExpression) last(queryableList).getFrom().getSqlTableExpression()).getAggregationType();
                return factory.dynamicColumn(value.toString(), aggregationType, tableRef);
            }
            else if (isUnPivotedName(method)) {
                ISqlUnPivotExpression unPivot = (ISqlUnPivotExpression) last(queryableList).getFrom().getSqlTableExpression();
                return factory.dynamicColumn(unPivot.getNewNameColumnName(), String.class, tableRef);
            }
            else if (isUnPivotedValue(method)) {
                ISqlUnPivotExpression unPivot = (ISqlUnPivotExpression) last(queryableList).getFrom().getSqlTableExpression();
                return factory.dynamicColumn(unPivot.getNewValueColumnName(), unPivot.getNewValueColumnType(), tableRef);
            }
            // else if (isDynamicColumn(method)) {
            //     List<Expression> args = methodCall.getArgs();
            //     String column = args.get(0).getValue().toString();
            //     Class<?> type = (Class<?>) args.get(1).getValue();
            //     return factory.dynamicColumn(column, type, tableRef);
            // }
        }
        // (a) -> a.jsonObjectField
        else if (left instanceof ISqlJsonObject) {
            ISqlJsonObject jsonObject = (ISqlJsonObject) left;
            MetaData metaData = config.getMetaData(method.getDeclaringClass());
            FieldMetaData getter = metaData.getFieldMetaDataByGetter(method);
            jsonObject.addJsonProperty(new JsonProperty(getter));
            return jsonObject;
        }
        // a.Navigate()
        else if (left instanceof ISqlQueryableExpression) {
            ISqlQueryableExpression leftQuery = (ISqlQueryableExpression) left;
            // ?.table().getter()
            if (isGetter(method)) {
                MetaData metaData = config.getMetaData(method.getDeclaringClass());
                FieldMetaData getter = metaData.getFieldMetaDataByGetter(method);
                if (getter.hasNavigate()) {
                    ISqlQueryableExpression query = queryToQueryable(leftQuery, getter);
                    // 弹出旧的
                    pop();
                    //  在子查询发起的地方压入
                    push(query);
                    return query;
                }
                else {
                    // select ? from table ...
                    // select table.getter from table ...
                    leftQuery.setSelect(factory.select(Collections.singletonList(factory.column(getter, leftQuery.getFrom().getTableRefExpression())), getter.getType()));
                    return leftQuery;
                }
            }
        }
        else if (isSqlExtensionExpressionMethod(method)) {
            SqlExtensionExpression sqlFuncExt = getSqlFuncExt(method.getAnnotationsByType(SqlExtensionExpression.class));
            List<ISqlExpression> expressions = new ArrayList<>(args.size());
            if (sqlFuncExt.extension() != BaseSqlExtension.class) {
                for (Expression arg : args) {
                    expressions.add(visit(arg));
                }
                BaseSqlExtension sqlExtension = BaseSqlExtension.getCache(sqlFuncExt.extension());
                boolean[] useSuper = {false};
                ISqlExpression parse = sqlExtension.parse(config, method, expressions, useSuper);
                if (useSuper[0]) {
                    parse = sqlExtension.callSuper(visit(methodCall.getExpr()), parse);
                }
                return parse;
            }
            else {
                List<String> strings = new ArrayList<>();
                List<Parameter> methodParameters = Arrays.stream(methodCall.getMethod().getParameters()).collect(Collectors.toList());
                ParamMatcher match = match(sqlFuncExt.template());
                List<String> functions = match.remainder;
                List<String> params = match.bracesContent;
                for (int i = 0; i < functions.size(); i++) {
                    strings.add(functions.get(i));
                    if (i < params.size()) {
                        String param = params.get(i);
                        // this应该获取前一步的结果
                        if (param.equals("super")) {
                            expressions.add(visit(methodCall.getExpr()));
                        }
                        else {
                            boolean isAnd = param.startsWith("&");
                            String paramFinal = isAnd ? param.substring(1) : param;
                            Parameter targetParam = methodParameters.stream()
                                    .filter(f -> f.getName().equals(paramFinal))
                                    .findFirst()
                                    .orElseThrow(() -> new DrinkException(String.format("无法在%s中找到%s", sqlFuncExt.template(), paramFinal)));
                            int index = methodParameters.indexOf(targetParam);

                            // 如果是可变参数
//                            System.out.println(targetParam);
                            boolean varArgs = targetParam.isVarArgs();
                            if (varArgs) {
                                while (index < args.size()) {
                                    if (isAnd) {
                                        parse$(args.get(index), strings);
                                    }
                                    else {
                                        expressions.add(visit(args.get(index)));
                                        if (index < args.size() - 1) {
                                            strings.add(sqlFuncExt.separator());
                                        }
                                        index++;
                                    }
                                }
                            }
                            // 正常情况
                            else {
                                if (isAnd) {
                                    parse$(args.get(index), strings);
                                }
                                else {
                                    expressions.add(visit(args.get(index)));
                                }
                            }
                        }
                    }
                }
                return factory.template(strings, expressions);
            }
        }
        else if (isSqlOperatorMethod(method)) {
            SqlOperatorMethod operatorMethod = method.getAnnotation(SqlOperatorMethod.class);
            SqlOperator operator = operatorMethod.value();
            if (operator == SqlOperator.BETWEEN) {
                ISqlExpression thiz = visit(args.get(0));
                ISqlExpression min = visit(args.get(1));
                ISqlExpression max = visit(args.get(2));
                return factory.binary(SqlOperator.BETWEEN, thiz, factory.binary(SqlOperator.AND, min, max));
            }
            else {
                if (operator.isLeft() || operator == SqlOperator.POSTINC || operator == SqlOperator.POSTDEC) {
                    ISqlExpression visit = visit(args.get(0));
                    if (visit instanceof ISqlQueryableExpression) {
                        visit = factory.unary(operator, visit);
                    }
                    return factory.unary(operator, visit);
                }
                else {
                    ISqlExpression l = visit(args.get(0));
                    ISqlExpression r = visit(args.get(1));
                    if (l instanceof ISqlQueryableExpression) {
                        l = factory.parens(l);
                    }
                    if (r instanceof ISqlQueryableExpression) {
                        r = factory.parens(r);
                    }
                    return factory.binary(operator, l, r);
                }
            }
        }
        else if (left instanceof JavaType) {
            JavaType javaType = (JavaType) left;
            Class<?> type = javaType.getType();
            if (Objects.class.isAssignableFrom(type)) {
                return objectsStaticHandler(methodCall);
            }
            else if (String.class.isAssignableFrom(type)) {
                return stringStaticHandler(methodCall);
            }
            else if (Math.class.isAssignableFrom(type)) {
                return mathStaticHandler(methodCall);
            }
            else if (DrinkUtil.isBigNumber(type)) {
                return bigNumberStaticHandler(methodCall);
            }
        }
        else if (left instanceof ISqlSingleValueExpression) {
            ISqlSingleValueExpression valueExpression = (ISqlSingleValueExpression) left;
            Object value = valueExpression.getValue();
            if (value instanceof String) {
                return stringHandler(methodCall);
            }
            else if (value instanceof BigInteger || value instanceof BigDecimal) {
                return bigNumberHandler(methodCall);
            }
            else if (value instanceof Temporal) {
                return dateTimeHandler(methodCall);
            }
            else if (value instanceof Date) {
                return oldDateTimeHandler(methodCall);
            }
            else if (value instanceof Collection) {
                return collectionHandler(methodCall);
            }
            else if (value instanceof Map) {
                return mapHandler(methodCall);
            }
            else if (isStartQuery(methodCall.getMethod())) {
                Expression expression = methodCall.getArgs().get(0);
                return visit(expression);
            }
            else if (value instanceof SqlClient) {
                String name = method.getName();
                Expression arg = args.get(0);
                if (name.equals("query") && arg.getKind() == Kind.StaticClass) {
                    StaticClassExpression staticClass = (StaticClassExpression) arg;
                    ISqlQueryableExpression queryable = factory.queryable(staticClass.getType());
                    push(queryable);
                    return queryable;
                }
            }
            else if (value instanceof QueryBase || isList(methodCall.getMethod())) {
                return queryOrDrinkListHandler(methodCall);
            }
            else {
                if (isEquals(methodCall)) {
                    return factory.binary(SqlOperator.EQ, visit(methodCall.getExpr()), visit(methodCall.getArgs().get(0)));
                }
            }
        }
        return checkAndReturnValue(methodCall);
    }

    /**
     * 二元运算表达式解析
     */
    @Override
    public ISqlExpression visit(BinaryExpression binary) {
        ISqlExpression left = visit(binary.getLeft());
        ISqlExpression right = visit(binary.getRight());
//        if (left instanceof ISqlQueryableExpression) {
//            left = factory.parens(left);
//        }
//        if (right instanceof ISqlQueryableExpression) {
//            right = factory.parens(right);
//        }
        return factory.binary(
                SqlOperator.valueOf(binary.getOperatorType().name()),
                left,
                right
        );
    }

    /**
     * 一元运算表达式解析
     */
    @Override
    public ISqlExpression visit(UnaryExpression unary) {
        return factory.unary(
                SqlOperator.valueOf(unary.getOperatorType().name()),
                visit(unary.getOperand())
        );
    }

    /**
     * 三元运算表达式解析
     */
    @Override
    public ISqlExpression visit(ConditionalExpression conditional) {
        ISqlExpression cond = visit(conditional.getCondition());
        ISqlExpression truePart = visit(conditional.getTruePart());
        ISqlExpression falsePart = visit(conditional.getFalsePart());
        return transformer.If(cond, truePart, falsePart);
    }

    /**
     * 括号表达式解析
     */
    @Override
    public ISqlExpression visit(ParensExpression parens) {
        return factory.parens(visit(parens.getExpr()));
    }

    /**
     * 常量表达式解析
     */
    @Override
    public ISqlExpression visit(ConstantExpression constant) {
        return factory.AnyValue(constant.getValue());
    }

    /**
     * 引用表达式解析
     */
    @Override
    public ISqlExpression visit(ReferenceExpression reference) {
        return factory.AnyValue(reference.getValue());
    }

    /**
     * new表达式解析
     */
    @Override
    public ISqlExpression visit(NewExpression newExpression) {
        BlockExpression classBody = newExpression.getClassBody();
        if (classBody == null) {
            return checkAndReturnValue(newExpression);
        }
        else {
            Class<?> type = newExpression.getType();
            // GROUP BY
            if (Grouper.class.isAssignableFrom(type)) {
                LinkedHashMap<String, ISqlExpression> contextMap = new LinkedHashMap<>();
                for (Expression expression : classBody.getExpressions()) {
                    if (expression.getKind() == Kind.Variable) {
                        VariableExpression variableExpression = (VariableExpression) expression;
                        String name = variableExpression.getName();
                        Expression init = variableExpression.getInit();
                        if (init != null) {
                            ISqlExpression sqlExpression = visit(init);
                            contextMap.put(name, sqlExpression);
                        }
                    }
                }
                return factory.groupBy(contextMap);
            }
            // new {...}
            else if (type.isAnonymousClass()) {
                Map<String, ISqlColumnExpression> valueNames = new HashMap<>();
                List<ISqlExpression> expressions = new ArrayList<>();
                MetaData metaData = config.getMetaData(type);
                for (Expression expression : classBody.getExpressions()) {
                    if (expression.getKind() == Kind.Variable) {
                        VariableExpression variable = (VariableExpression) expression;
                        String varName = variable.getName();
                        FieldMetaData fieldMetaData = metaData.getFieldMetaDataByFieldName(varName);
                        Expression init = variable.getInit();
                        if (init != null) {
                            ISqlExpression visit = visit(init);
                            // a.b() 导航属性
                            // a.query(a.b()).where(...)...toList()/first() 导航属性后附带条件
                            // client.query(...)...toList()/first() 子查询
                            saveSelectOrSubQuery(visit, init, expressions, varName, fieldMetaData, valueNames);
                        }
                    }
                    else if (expression.getKind() == Kind.Block) {
                        BlockExpression blockExpression = (BlockExpression) expression;
                        for (Expression bb : blockExpression.getExpressions()) {
                            if (bb.getKind() == Kind.MethodCall) {
                                MethodCallExpression methodCall = (MethodCallExpression) bb;
                                Expression expr = methodCall.getExpr();
                                Method method = methodCall.getMethod();
                                // this == null
                                if (expr == null && isSetter(method)) {
                                    FieldMetaData setter = metaData.getFieldMetaDataBySetter(method);
                                    String varName = setter.getFieldName();
                                    FieldMetaData fieldMetaData = metaData.getFieldMetaDataByFieldName(varName);
                                    List<Expression> args = methodCall.getArgs();
                                    if (!args.isEmpty()) {
                                        Expression arg = args.get(0);
                                        ISqlExpression visit = visit(arg);
                                        saveSelectOrSubQuery(visit, arg, expressions, varName, fieldMetaData, valueNames);
                                    }
                                }
                            }
                        }
                    }
                }
                // 子查询需要的额外字段
                ISqlSelectExpression lastSelect = last(queryableList).getSelect();
                ExValues exValues = lastSelect.getExValues();
                for (Map.Entry<String, ISqlColumnExpression> entry : valueNames.entrySet()) {
                    // select ...,? as `value:xx`
                    expressions.add(factory.as(entry.getValue(), entry.getKey()));
                    // 保存
                    exValues.addExValue(entry.getKey(), entry.getValue().getFieldMetaData());
                }
                return factory.select(expressions, newExpression.getType());
            }
            else {
                return checkAndReturnValue(newExpression);
            }
        }
    }

    private void saveSelectOrSubQuery(ISqlExpression visit, Expression variable, List<ISqlExpression> expressions, String varName, FieldMetaData fieldMetaData, Map<String, ISqlColumnExpression> valueNames) {
        if (visit instanceof ISqlQueryableExpression) {
            ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
            boolean agg = queryable.getSelect().isAgg();
            if (agg) {
                // 某些数据库不支持直接返回bool类型，所以需要做一下包装
                visit = tryToBool(variable, visit);
                setAs(expressions, visit, varName);
            }
            else {
                ISqlQueryableExpression current = last(queryableList);
                List<ISqlTableRefExpression> currentTableRefs = last(tableRefListList);
                queryable.accept(new SqlTreeTransformer(config) {
                    // 把上层级的字段调用转换成值
                    @Override
                    public ISqlExpression visit(ISqlColumnExpression expr) {
                        ISqlTableRefExpression tableRef = expr.getTableRefExpression();
                        if (currentTableRefs.contains(tableRef)) {
                            SubQueryValue sq = new SubQueryValue(expr.getFieldMetaData(), tableRefListList.size() - 1);
                            String valueName = sq.getValueName();
                            if (!valueNames.containsKey(valueName)) {
                                valueNames.put(valueName, expr);
                            }
                            return sq;
                        }
                        return expr;
                    }

                    // 递归进入到子表查询
                    @Override
                    public ISqlExpression visit(ISqlSelectExpression expr) {
                        for (SubQueryBuilder subQueryBuilder : expr.getSubQueryBuilders()) {
                            ISqlQueryableExpression queryable = subQueryBuilder.getQueryableExpression();
                            ISqlQueryableExpression visit = (ISqlQueryableExpression) visit(queryable);
                            if (visit != queryable) {
                                subQueryBuilder.setQueryableExpression(visit);
                            }
                        }
                        return super.visit(expr);
                    }
                });
                ISqlSelectExpression select = current.getSelect();
                select.addSubQueryBuilder(new SubQueryBuilder(config, fieldMetaData, queryable));
            }
        }
        else {
            // 某些数据库不支持直接返回bool类型，所以需要做一下包装
            visit = tryToBool(variable, visit);
            setAs(expressions, visit, varName);
        }
    }

    @Override
    public ISqlExpression visit(ParameterExpression parameter) {
        if (IGroup.class.isAssignableFrom(parameter.getType())) {
            return new SqlGroupRef();
        }
        else if (Aggregate.class.isAssignableFrom(parameter.getType())) {
            return last(tableRefListList).get(0);
        }
        else {
            return getISqlTableRefExpressionByIndex(parameter);
        }
    }

    @Override
    public ISqlExpression visit(TypeCastExpression typeCast) {
        return factory.typeCast(typeCast.getTargetType(), visit(typeCast.getExpr()));
    }

    @Override
    public ISqlExpression visit(StaticClassExpression staticClassExpression) {
        return new JavaType(staticClassExpression.getType());
    }

    protected ParamMatcher match(String input) {
        ParamMatcher paramMatcher = new ParamMatcher();

        List<String> bracesContent = paramMatcher.bracesContent;
        List<String> remainder = paramMatcher.remainder;
        // 正则表达式匹配"{}"内的内容
        Pattern pattern = Pattern.compile("\\{([^}]+)}");
        Matcher matcher = pattern.matcher(input);

        int lastIndex = 0; // 上一个匹配项结束的位置
        while (matcher.find()) {
            // 添加上一个匹配项到剩余字符串（如果有的话）
            if (lastIndex < matcher.start()) {
                remainder.add(input.substring(lastIndex, matcher.start()));
            }

            // 提取并添加"{}"内的内容
            bracesContent.add(matcher.group(1));

            // 更新上一个匹配项结束的位置
            lastIndex = matcher.end();
        }

        // 添加最后一个匹配项之后的剩余字符串（如果有的话）
        if (lastIndex < input.length()) {
            remainder.add(input.substring(lastIndex));
        }

        if (input.startsWith("{")) remainder.add(0, "");
        if (input.endsWith("}")) remainder.add("");

        return paramMatcher;
    }

    protected ISqlQueryableExpression getterToQueryable(FieldMetaData getter, ISqlTableRefExpression selfISqlTableRefExpression) {
        // A.B();
        // FROM A WHERE (SELECT ... FROM B WHERE B.ID = (SELECT A.ID FROM A))
        //                                                V
        //                                                V
        // FROM A WHERE (SELECT ... FROM B WHERE B.ID = A.ID)
//        FieldMetaData fieldMetaData = columnExpression.getFieldMetaData();
//        ISqlRealTableExpression table;
//        ISqlConditionsExpression condition = null;
//        ISqlTableRefExpression mainTableISqlTableRefExpression = columnExpression.getTableISqlTableRefExpression();
//        ISqlTableRefExpression subTableISqlTableRefExpression;
//        Set<String> stringSet = new HashSet<>();
//        stringSet.add(fromExpression.getISqlTableRefExpression().getName());
//        for (ISqlJoinExpression join : joinsExpression.getJoins()) {
//            stringSet.add(join.getISqlTableRefExpression().getName());
//        }
//        for (ISqlTableRefExpression asName : asNameDeque) {
//            stringSet.add(asName.getName());
//        }
//        //String subTableISqlTableRefExpression = doGetISqlTableRefExpression(config.getMetaData(getTargetType(fieldMetaData.getGenericType())).getTableName().toLowerCase().substring(0, 1));
//        if (fieldMetaData.hasNavigate()) {
//            NavigateData navigateData = fieldMetaData.getNavigateData();
//            Class<?> targetType = navigateData.getNavigateTargetType();
//            table = factory.table(targetType);
//            subTableISqlTableRefExpression = doGetISqlTableRefExpression(getFirst(targetType), stringSet);
//            MetaData targetMetaData = config.getMetaData(targetType);
//            condition = factory.condition();
//            FieldMetaData targetFieldMetaData = targetMetaData.getFieldMetaDataByFieldName(navigateData.getTargetFieldName());
//            FieldMetaData selfFieldMetaData = config.getMetaData(fieldMetaData.getParentType()).getFieldMetaDataByFieldName(navigateData.getSelfFieldName());
//            condition.addCondition(factory.binary(SqlOperator.EQ, factory.column(targetFieldMetaData, subTableISqlTableRefExpression), factory.column(selfFieldMetaData, mainTableISqlTableRefExpression)));
//        }
//        else {
//            Type genericType = fieldMetaData.getGenericType();
//            table = factory.table(getTargetType(genericType));
//            subTableISqlTableRefExpression = doGetISqlTableRefExpression(getFirst(table.getMainTableClass()), stringSet);
//        }
//        ISqlQueryableExpression queryable = factory.queryable(factory.from(table, subTableISqlTableRefExpression));
//        if (condition != null) {
//            queryable.addWhere(condition);
//        }
//        return queryable;

        NavigateData navigateData = getter.getNavigateData();
        String selfFieldName = navigateData.getSelfFieldName();
        String targetFieldName = navigateData.getTargetFieldName();
        Class<?> selfType = getter.getParentType();
        Class<?> targetType = navigateData.getNavigateTargetType();
        FieldMetaData selfField = config.getMetaData(selfType).getFieldMetaDataByFieldName(selfFieldName);
        FieldMetaData targetField = config.getMetaData(targetType).getFieldMetaDataByFieldName(targetFieldName);
        RelationType relationType = navigateData.getRelationType();

        ISqlQueryableExpression subQuery = factory.queryable(targetType);
        // A.B
        // 一对一，一对多，多对一的场合 [A = B]
        // SELECT B.* FROM B WHERE B.targetField = A.selfField
        // one to many的场合 [A IN {B1,B2,B3,...}]
        // SELECT B.* FROM B WHERE B.targetField = A.selfField
        if (relationType == RelationType.ManyToOne || relationType == RelationType.OneToOne || relationType == RelationType.OneToMany) {
            subQuery.addWhere(factory.binary(SqlOperator.EQ, factory.column(targetField, subQuery.getFrom().getTableRefExpression()), factory.column(selfField, selfISqlTableRefExpression)));
        }
        // many to many的场合
        // SELECT B.* FROM B WHERE B.targetField IN (SELECT M.targetMapping FROM M WHERE M.selfMapping = A.selfField)
        // TODO:优化成以下
        // SELECT B.* FROM B INNER JOIN M ON B.targetField = M.targetMapping WHERE M.selfMapping = A.selfField
        else {
            FieldMetaData selfMappingField = navigateData.getSelfMappingFieldMetaData(config);
            FieldMetaData targetMappingField = navigateData.getTargetMappingFieldMetaData(config);
            Class<? extends IMappingTable> mappingType = navigateData.getMappingTableType();
            ISqlQueryableExpression mappingQuery = factory.queryable(mappingType);
            ISqlTableRefExpression mappingTableRef = mappingQuery.getFrom().getTableRefExpression();
            mappingQuery.addWhere(factory.binary(SqlOperator.EQ, factory.column(selfMappingField, mappingTableRef), factory.column(selfField, selfISqlTableRefExpression)));
            mappingQuery.setSelect(factory.select(new ArrayList<>(Collections.singletonList(factory.column(targetMappingField, mappingTableRef))), targetMappingField.getType()));
            subQuery.addWhere(factory.binary(SqlOperator.IN, factory.column(targetField, subQuery.getFrom().getTableRefExpression()), mappingQuery));
        }
        return subQuery;
    }

    protected ISqlQueryableExpression queryToQueryable(ISqlQueryableExpression left, FieldMetaData targetField) {
        NavigateData navigateData = targetField.getNavigateData();
        RelationType relationType = navigateData.getRelationType();
        Class<?> targetType = targetField.getType();
        FieldMetaData selfField = config.getMetaData(targetField.getParentType()).getFieldMetaDataByFieldName(navigateData.getSelfFieldName());
        List<ISqlTableRefExpression> asNameList = last(tableRefListList);

        left.setSelect(factory.select(Collections.singletonList(factory.column(selfField, left.getFrom().getTableRefExpression())), selfField.getType()));
        ISqlQueryableExpression subQuery = factory.queryable(targetType);

        if (relationType == RelationType.ManyToOne || relationType == RelationType.OneToOne || relationType == RelationType.OneToMany) {
            subQuery.addWhere(factory.binary(SqlOperator.IN, factory.column(targetField, subQuery.getFrom().getTableRefExpression()), left));
        }
        else {
            FieldMetaData selfMappingField = navigateData.getSelfMappingFieldMetaData(config);
            FieldMetaData targetMappingField = navigateData.getTargetMappingFieldMetaData(config);
            Class<? extends IMappingTable> mappingType = navigateData.getMappingTableType();
            ISqlQueryableExpression mappingQuery = factory.queryable(mappingType);
            mappingQuery.addWhere(factory.binary(SqlOperator.IN, factory.column(selfMappingField, mappingQuery.getFrom().getTableRefExpression()), left));
            mappingQuery.setSelect(factory.select(new ArrayList<>(Collections.singletonList(factory.column(targetMappingField, mappingQuery.getFrom().getTableRefExpression()))), targetMappingField.getType()));
            subQuery.addWhere(factory.binary(SqlOperator.IN, factory.column(targetField, subQuery.getFrom().getTableRefExpression()), mappingQuery));
        }

        return subQuery;
    }

    protected void setAs(List<ISqlExpression> contexts, ISqlExpression expression, String name) {
        if (expression instanceof ISqlColumnExpression) {
            ISqlColumnExpression sqlColumn = (ISqlColumnExpression) expression;
            if (!sqlColumn.getFieldMetaData().getColumn().equals(name)) {
                contexts.add(factory.as(expression, name));
            }
            else {
                contexts.add(expression);
            }
        }
        else {
            contexts.add(factory.as(expression, name));
        }
    }

    protected ISqlExpression tryToBool(Expression init, ISqlExpression result) {
        if (init instanceof MethodCallExpression) {
            MethodCallExpression methodCall = (MethodCallExpression) init;
            return tryToBool(isBool(methodCall.getMethod().getReturnType()), result);
        }
        else if (init instanceof UnaryExpression) {
            UnaryExpression unary = (UnaryExpression) init;
            return tryToBool(unary.getOperatorType() == OperatorType.NOT, result);
        }
        return result;
    }

    protected ISqlExpression tryToBool(boolean condition, ISqlExpression result) {
        if (!condition) return result;
        return config.getTransformer().boxBool(result);
    }

    public ISqlSelectExpression toSelect(LambdaExpression<?> lambda) {
        ISqlExpression expression = visit(lambda);
        ISqlSelectExpression selectExpression;
        if (expression instanceof ISqlSelectExpression) {
            selectExpression = (ISqlSelectExpression) expression;
        }
        else if (expression instanceof ISqlTableRefExpression) {
            ISqlTableRefExpression tableRef = (ISqlTableRefExpression) expression;
            List<ISqlTableRefExpression> peek = last(tableRefListList);
            int index = peek.indexOf(tableRef);
            Class<?> tableClass;
            ISqlQueryableExpression queryable = queryableList.get(0);
            if (index == 0) {
                tableClass = queryable.getFrom().getSqlTableExpression().getMainTableClass();
            }
            else {
                tableClass = queryable.getJoins().getJoins().get(index - 1).getJoinTable().getMainTableClass();
            }
            selectExpression = factory.select(tableClass, tableRef);
        }
        else if (expression instanceof ISqlGroupByExpression) {
            ISqlGroupByExpression groupBy = (ISqlGroupByExpression) expression;
            selectExpression = factory.select(new ArrayList<>(groupBy.getColumns().values()), lambda.getReturnType());
        }
        else if (expression instanceof SqlGroupRef) {
            throw new DrinkException("ISqlGroupRef");
        }
        else {
            SqlExpressionFactory factory = config.getSqlExpressionFactory();
            // 用于包装某些数据库不支持直接返回bool
            if (isBool(lambda.getReturnType())) {
                expression = config.getTransformer().boxBool(expression);
            }
            selectExpression = factory.select(Collections.singletonList(expression), lambda.getReturnType(), true, false);
        }
        return selectExpression;
    }

//    public Map<String, QuerySqlBuilder> getSubQueryMap() {
//        return subQueryMap;
//    }

    public ISqlGroupByExpression toGroup(LambdaExpression<?> lambda) {
        ISqlExpression expression = visit(lambda);
        ISqlGroupByExpression group;
        if (expression instanceof ISqlGroupByExpression) {
            group = (ISqlGroupByExpression) expression;
        }
        else {
            throw new SqLinkException(String.format("意外的类型:%s 表达式为:%s", expression.getClass(), lambda));
        }
        return group;
    }

    public ISqlColumnExpression toColumn(LambdaExpression<?> lambda) {
        ISqlExpression expression = visit(lambda);
        ISqlColumnExpression column;
        if (expression instanceof ISqlColumnExpression) {
            column = (ISqlColumnExpression) expression;
        }
        else {
            throw new SqLinkException(String.format("意外的类型:%s 表达式为:%s", expression.getClass(), lambda));
        }
        return column;
    }

    public FieldMetaData toField(LambdaExpression<?> lambda) {
        Expression body = lambda.getBody();
        if (body instanceof FieldSelectExpression) {
            FieldSelectExpression fieldSelect = (FieldSelectExpression) body;
            if (fieldSelect.getExpr() instanceof ParameterExpression) {
                ParameterExpression expr = (ParameterExpression) fieldSelect.getExpr();
                return config.getMetaData(expr.getType()).getFieldMetaDataByFieldName(fieldSelect.getField().getName());
            }
        }
        else if (body instanceof MethodCallExpression) {
            MethodCallExpression methodCall = (MethodCallExpression) body;
            if (methodCall.getExpr() instanceof ParameterExpression) {
                ParameterExpression expr = (ParameterExpression) methodCall.getExpr();
                return config.getMetaData(expr.getType()).getFieldMetaDataByGetter(methodCall.getMethod());
            }
        }
        throw new DrinkException(lambda.toString());
    }

    protected ISqlTableRefExpression getISqlTableRefExpressionByIndex(ParameterExpression parameter) {
        ISqlTableRefExpression asName;
        if (index >= 0) {
            asName = last(tableRefListList).get(index);
        }
        else {
            asName = asNameMap.get(parameter);
        }
        return asName;
    }

    protected ISqlQueryableExpression getterToSqlAst(FieldMetaData getter, ISqlTableRefExpression tableRef) {
        // select * from target as t where t.targetField = s.selfField
        ISqlQueryableExpression query = getterToQueryable(getter, tableRef);
        //  在子查询发起的地方压入
        push(query);
        return query;
    }

    protected FieldMetaData methodToFieldMetaData(Method method, ISqlTableRefExpression tableRef) {
        Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass.isInterface()) {
            List<ISqlTableRefExpression> peek = last(tableRefListList);
            int index = peek.indexOf(tableRef);
            Class<?> tableClass;
            if (index == 0) {
                tableClass = last(queryableList).getFrom().getType();
            }
            else {
                tableClass = last(queryableList).getJoins().getJoins().get(index - 1).getJoinTable().getType();
            }
            MetaData metaData = config.getMetaData(tableClass);
            return metaData.getFieldMetaDataByGetterName(method.getName());
        }
        else {
            MetaData metaData = config.getMetaData(declaringClass);
            return metaData.getFieldMetaDataByGetter(method);
        }
    }

    protected void parse$(Expression arg, List<String> strings) {
        ISqlExpression visit = visit(arg);
        if (visit instanceof ISqlSingleValueExpression) {
            ISqlSingleValueExpression singleValue = (ISqlSingleValueExpression) visit;
            strings.add(singleValue.getValue().toString());
        }
        else if (visit instanceof ISqlCollectedValueExpression) {
            ISqlCollectedValueExpression collectedValue = (ISqlCollectedValueExpression) visit;
            strings.add(collectedValue.getCollection().toString());
        }
        else {
            throw new DrinkException("raw参数必须是求值的");
        }
    }

    public ISqlTemplateExpression toAgg(LambdaExpression<?> aggColumn) {
        ISqlExpression visit = visit(aggColumn);
        if (visit instanceof ISqlTemplateExpression) {
            return (ISqlTemplateExpression) visit;
        }
        else {
            throw new DrinkException(String.format("%s无法转换成一个聚合函数", aggColumn));
        }
    }

    private static class SqlGroupRef implements ISqlExpression {
        @Override
        public String getSqlAndValue(IConfig config, List<SqlValue> values) {
            return "";
        }

        @Override
        public <T extends ISqlExpression> T copy(IConfig config) {
            return null;
        }
    }

//    private static class QueryBox implements ISqlQueryableExpression {
//        private final ISqlQueryableExpression queryable;
//        private final QuerySqlBuilder querySqlBuilder;
//        private boolean isSingleRowResult;
//
//        private QueryBox(IConfig config, ISqlQueryableExpression queryable) {
//            this.queryable = queryable;
//            querySqlBuilder = new QuerySqlBuilder(config, queryable);
//        }
//
//        public QuerySqlBuilder getQuerySqlBuilder() {
//            return querySqlBuilder;
//        }
//
//        public ISqlQueryableExpression getQueryable() {
//            return queryable;
//        }
//
//        public List<IncludeBuilder> getIncludes() {
//            return querySqlBuilder.getIncludes();
//        }
//
//        public boolean isSingleRowResult() {
//            return isSingleRowResult;
//        }
//
//        public void setSingleRowResult(boolean singleRowResult) {
//            isSingleRowResult = singleRowResult;
//        }
//
//        @Override
//        public String getSqlAndValue(IConfig config, List<SqlValue> values) {
//            return queryable.getSqlAndValue(config, values);
//        }
//
//        @Override
//        public void addWhere(ISqlExpression cond) {
//            queryable.addWhere(cond);
//        }
//
//        @Override
//        public void setWhere(ISqlConditionsExpression conditions) {
//            queryable.setWhere(conditions);
//        }
//
//        @Override
//        public void addJoin(ISqlJoinExpression join) {
//            queryable.addJoin(join);
//        }
//
//        @Override
//        public void setGroup(ISqlGroupByExpression group) {
//            queryable.setGroup(group);
//        }
//
//        @Override
//        public void addHaving(ISqlExpression cond) {
//            queryable.addHaving(cond);
//        }
//
//        @Override
//        public void addOrder(ISqlOrderExpression order) {
//            queryable.addOrder(order);
//        }
//
//        @Override
//        public void setSelect(ISqlSelectExpression newSelect) {
//            queryable.setSelect(newSelect);
//        }
//
//        @Override
//        public void setLimit(long offset, long rows) {
//            queryable.setLimit(offset, rows);
//        }
//
//        @Override
//        public void setDistinct(boolean distinct) {
//            queryable.setDistinct(distinct);
//        }
//
//        @Override
//        public ISqlFromExpression getFrom() {
//            return queryable.getFrom();
//        }
//
//        @Override
//        public int getOrderedCount() {
//            return queryable.getOrderedCount();
//        }
//
//        @Override
//        public ISqlWhereExpression getWhere() {
//            return queryable.getWhere();
//        }
//
//        @Override
//        public ISqlGroupByExpression getGroupBy() {
//            return queryable.getGroupBy();
//        }
//
//        @Override
//        public ISqlJoinsExpression getJoins() {
//            return queryable.getJoins();
//        }
//
//        @Override
//        public ISqlSelectExpression getSelect() {
//            return queryable.getSelect();
//        }
//
//        @Override
//        public ISqlOrderByExpression getOrderBy() {
//            return queryable.getOrderBy();
//        }
//
//        @Override
//        public ISqlLimitExpression getLimit() {
//            return queryable.getLimit();
//        }
//
//        @Override
//        public ISqlHavingExpression getHaving() {
//            return queryable.getHaving();
//        }
//
//        @Override
//        public Class<?> getMainTableClass() {
//            return queryable.getMainTableClass();
//        }
//    }

    private List<ISqlTableRefExpression> getTableRefs(ISqlQueryableExpression queryable) {
        List<ISqlTableRefExpression> list = new ArrayList<>();

        ISqlFromExpression from = queryable.getFrom();
        ISqlJoinsExpression joins = queryable.getJoins();

        list.add(from.getTableRefExpression());
        for (ISqlJoinExpression join : joins.getJoins()) {
            list.add(join.getTableRefExpression());
        }

        return list;
    }
}
