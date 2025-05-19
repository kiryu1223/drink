/*
 * Copyright 2017-2024 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.metaData.*;
import io.github.kiryu1223.drink.base.sqlExt.BaseSqlExtension;
import io.github.kiryu1223.drink.base.sqlExt.SqlExtensionExpression;
import io.github.kiryu1223.drink.base.sqlExt.SqlOperatorMethod;
import io.github.kiryu1223.drink.base.transform.*;
import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.drink.core.api.crud.read.QueryBase;
import io.github.kiryu1223.drink.core.api.crud.read.group.Grouper;
import io.github.kiryu1223.drink.core.api.crud.read.group.IGroup;
import io.github.kiryu1223.drink.core.exception.SqLinkException;
import io.github.kiryu1223.drink.core.exception.SqLinkIllegalExpressionException;
import io.github.kiryu1223.drink.core.exception.SqlFuncExtNotFoundException;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.*;

/**
 * 表达式解析器
 *
 * @author kiryu1223
 * @since 3.0
 */
public class SqlVisitor extends ResultThrowVisitor<ISqlExpression> {
    protected final IConfig config;
    protected final SqlExpressionFactory factory;
    //    protected final ISqlFromExpression fromExpression;
//    protected final ISqlJoinsExpression joinsExpression;
//    protected boolean isFirst = true;
//    protected boolean isGroup = false;
    protected int index = -1;
    //    protected final List<ISqlTableRefExpression> asNameList = new ArrayList<>();
//    protected final List<ParameterExpression> parameterExpressionList = new ArrayList<>();
//    protected final Map<String,ISqlGroupByExpression> groupMap=new HashMap<>();
    protected final Deque<List<ISqlTableRefExpression>> asNameListDeque = new ArrayDeque<>();
    protected final Map<ParameterExpression, ISqlTableRefExpression> asNameMap = new HashMap<>();
    protected final Deque<ISqlGroupByExpression> groupDeque = new ArrayDeque<>();
    protected final Deque<ISqlFromExpression> fromDeque = new ArrayDeque<>();
    protected final Deque<ISqlJoinsExpression> joinsDeque = new ArrayDeque<>();
    protected final Map<ParameterExpression, ISqlGroupByExpression> groupMap = new HashMap<>();

    public SqlVisitor(IConfig config, ISqlQueryableExpression sqlQueryableExpression) {
        this(config, sqlQueryableExpression.getFrom(), sqlQueryableExpression.getJoins(), sqlQueryableExpression.getGroupBy(), -1);
    }

    public SqlVisitor(IConfig config, ISqlQueryableExpression sqlQueryableExpression, int index) {
        this(config, sqlQueryableExpression.getFrom(), sqlQueryableExpression.getJoins(), sqlQueryableExpression.getGroupBy(), index);
    }

    public SqlVisitor(IConfig config, ISqlUpdateExpression updateExpression) {
        this(config, updateExpression.getFrom(), updateExpression.getJoins(), null, -1);
    }

    public SqlVisitor(IConfig config, ISqlDeleteExpression deleteExpression) {
        this(config, deleteExpression.getFrom(), deleteExpression.getJoins(), null, -1);
    }

    protected SqlVisitor(IConfig config, ISqlFromExpression fromExpression, ISqlJoinsExpression joinsExpression, ISqlGroupByExpression groupByExpression, int index) {
        this.config = config;
        this.factory = config.getSqlExpressionFactory();
        List<ISqlTableRefExpression> asNameList = new ArrayList<>();
        asNameList.add(fromExpression.getTableRefExpression());
        for (ISqlJoinExpression join : joinsExpression.getJoins()) {
            asNameList.add(join.getTableRefExpression());
        }
        push(asNameList,fromExpression,joinsExpression,groupByExpression);
    }

    protected void push(ISqlQueryableExpression queryable)
    {
        ISqlFromExpression from = queryable.getFrom();
        ISqlJoinsExpression joins = queryable.getJoins();
        List<ISqlTableRefExpression> asNameList = new ArrayList<>();
        asNameList.add(from.getTableRefExpression());
        for (ISqlJoinExpression join : joins.getJoins()) {
            asNameList.add(join.getTableRefExpression());
        }
        push(asNameList,from,joins,queryable.getGroupBy());
    }

    protected void push(List<ISqlTableRefExpression> tableRefExpressions,ISqlFromExpression fromExpression,ISqlJoinsExpression joinsExpression,ISqlGroupByExpression groupByExpression)
    {
        asNameListDeque.push(tableRefExpressions);
        groupDeque.push(groupByExpression);
        fromDeque.push(fromExpression);
        joinsDeque.push(joinsExpression);
    }

    protected void pop()
    {
        asNameListDeque.pop();
        groupDeque.pop();
        fromDeque.pop();
        joinsDeque.pop();
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
            groupMap.put(parameterExpression, groupDeque.peek());
            ISqlExpression visit = visit(lambda.getBody());
            groupMap.remove(parameterExpression);
            return visit;
        }
        // (a,b,c) -> ...
        else {
            List<ISqlTableRefExpression> peek = asNameListDeque.peek();
            for (int i = 0; i < parameters.size(); i++) {
                asNameMap.put(parameters.get(i), peek.get(i));
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
        if (left instanceof ISqlGroupRef) {
            // g.key
            if (fieldName.equals("key")) {
                return groupDeque.peek();
            }
            // g.value?
            else if (fieldName.startsWith("value")) {
                int index = Integer.parseInt(fieldName.replace("value", ""));
                List<ISqlTableRefExpression> list = asNameListDeque.peek();
                return list.get(index-1);
            }
            else
            {
                throw new DrinkException(fieldSelect.toString());
            }
        }
        else if (left instanceof ISqlGroupByExpression)
        {
            ISqlGroupByExpression group = (ISqlGroupByExpression) left;
            return group.getColumns().get(fieldName);
        }
        else if (left instanceof ISqlTableRefExpression)
        {
            ISqlTableRefExpression tableRef = (ISqlTableRefExpression) left;
            MetaData metaData = config.getMetaData(field.getDeclaringClass());
            FieldMetaData fieldMetaData = metaData.getFieldMetaDataByFieldName(field.getName());
            if (fieldMetaData.hasNavigate())
            {
                return getterToSqlAst(fieldMetaData, tableRef);
            }
            else
            {
                return factory.column(fieldMetaData, tableRef);
            }
        }
        else
        {
            return checkAndReturnValue(fieldSelect);
        }
//        if (isGroupKey(asNameMap, fieldSelect.getExpr())) // g.key.field
//        {
//            FieldSelectExpression fieldSelectExpression = (FieldSelectExpression) fieldSelect.getExpr();
//            ISqlGroupByExpression groupBy = groupMap.get((ParameterExpression) fieldSelectExpression.getExpr());
//            Map<String, ISqlExpression> columns = groupBy.getColumns();
//            return columns.get(fieldSelect.getField().getName());
//        }
//        else if (isGroupValue(asNameMap, fieldSelect)) // g.value?
//        {
//            String name = fieldSelect.getField().getName();
//            int valueIndex = Integer.parseInt(name.replace("value", ""));
//            List<ISqlTableRefExpression> asNameList = asNameListDeque.peek();
//            return asNameList.get(valueIndex);
////            FieldSelectExpression expr = (FieldSelectExpression) fieldSelect.getExpr();
////            String vname = expr.getField().getName();
////            int valueIndex = Integer.parseInt(vname.replace("value", ""));
////            List<ISqlTableRefExpression> asNameList = asNameListDeque.peek();
////            ISqlTableRefExpression asName = asNameList.get(valueIndex);
////            Field field = fieldSelect.getField();
////            MetaData metaData = config.getMetaData(field.getDeclaringClass());
////            return factory.column(metaData.getFieldMetaDataByFieldName(field.getName()), asName);
//        }
//        else if (isProperty(asNameMap, fieldSelect)) {
//            ParameterExpression parameter = (ParameterExpression) fieldSelect.getExpr();
//            Field field = fieldSelect.getField();
//            MetaData metaData = config.getMetaData(field.getDeclaringClass());
//            ISqlTableRefExpression asName = getISqlTableRefExpressionByIndex(parameter);
//            return factory.column(metaData.getFieldMetaDataByFieldName(field.getName()), asName);
//        }
//        else {
//            return checkAndReturnValue(fieldSelect);
//        }
    }

    /**
     * 方法调用表达式解析
     */
    @Override
    public ISqlExpression visit(MethodCallExpression methodCall) {
        // equals
        if (isEquals(methodCall)) {
            return factory.binary(SqlOperator.EQ, visit(methodCall.getExpr()), visit(methodCall.getArgs().get(0)));
        }
        // SQL扩展函数
        else if (isSqlExtensionExpressionMethod(methodCall.getMethod())) {
            Method sqlFunction = methodCall.getMethod();
            SqlExtensionExpression sqlFuncExt = getSqlFuncExt(sqlFunction.getAnnotationsByType(SqlExtensionExpression.class));
            List<Expression> args = methodCall.getArgs();
            List<ISqlExpression> expressions = new ArrayList<>(args.size());
            if (sqlFuncExt.extension() != BaseSqlExtension.class) {
                for (Expression arg : args) {
                    expressions.add(visit(arg));
                }
                BaseSqlExtension sqlExtension = BaseSqlExtension.getCache(sqlFuncExt.extension());
                return sqlExtension.parse(config, sqlFunction, expressions);
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
                            Parameter targetParam = methodParameters.stream()
                                    .filter(f -> f.getName().equals(param))
                                    .findFirst()
                                    .orElseThrow(() -> new DrinkException(String.format("无法在%s中找到%s", sqlFuncExt.template(), param)));
                            int index = methodParameters.indexOf(targetParam);

                            // 如果是可变参数
                            if (targetParam.isVarArgs()) {
                                while (index < args.size()) {
                                    expressions.add(visit(args.get(index)));
                                    if (index < args.size() - 1) strings.add(sqlFuncExt.separator());
                                    index++;
                                }
                            }
                            // 正常情况
                            else {
                                expressions.add(visit(args.get(index)));
                            }
                        }
                    }
                }
                return factory.template(strings, expressions);
            }
        }
        // SQL运算符
        else if (isSqlOperatorMethod(methodCall.getMethod())) {
            Method method = methodCall.getMethod();
            List<Expression> args = methodCall.getArgs();
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
                    ISqlExpression left = visit(methodCall.getArgs().get(0));
                    ISqlExpression right = visit(methodCall.getArgs().get(1));
                    if (left instanceof ISqlQueryableExpression) {
                        left = factory.parens(left);
                    }
                    if (right instanceof ISqlQueryableExpression) {
                        right = factory.parens(right);
                    }
                    return factory.binary(operator, left, right);
                }
            }
        }
        // 子查询发起者
        else if (isStartQuery(methodCall.getMethod())) {
            Expression expression = methodCall.getArgs().get(0);
            return visit(expression);
        }
        else if (SqlClient.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            Method method = methodCall.getMethod();
            String methodName = method.getName();
            Expression expression = methodCall.getArgs().get(0);
            if (methodName.equals("query")&&expression.getKind()==Kind.StaticClass) {
                StaticClassExpression staticClass = (StaticClassExpression) expression;
                ISqlQueryableExpression queryable = factory.queryable(staticClass.getType());
                push(queryable);
                return queryable;
            }
            return checkAndReturnValue(methodCall);
        }
        // 子查询
        else if (QueryBase.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            Method method = methodCall.getMethod();
            String methodName = method.getName();
            if (methodName.equals("count"))
            {
                ISqlExpression visit = visit(methodCall.getExpr());
                if (!(visit instanceof ISqlQueryableExpression))
                {
                    throw new SqLinkException("不支持的表达式:" + methodCall);
                }
                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
                ISqlExpression column = null;
                if (!methodCall.getArgs().isEmpty())
                {
                    column = visit(methodCall.getArgs().get(0));
                }
                IAggregateMethods agg = config.getTransformer();
                queryable.setSelect(factory.select(Collections.singletonList(agg.count(column)), long.class));
                // 在终结的地方弹出
                pop();
                return queryable;
            }
            else if (methodName.equals("sum"))
            {
                ISqlExpression visit = visit(methodCall.getExpr());
                if (!(visit instanceof ISqlQueryableExpression))
                {
                    throw new SqLinkException("不支持的表达式:" + methodCall);
                }
                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
                ISqlExpression column = visit(methodCall.getArgs().get(0));
                IAggregateMethods agg = config.getTransformer();
                queryable.setSelect(factory.select(Collections.singletonList(agg.sum(column)), BigDecimal.class));
                // 在终结的地方弹出
                pop();
                return queryable;
            }
            else if (methodName.equals("avg"))
            {
                ISqlExpression visit = visit(methodCall.getExpr());
                if (!(visit instanceof ISqlQueryableExpression))
                {
                    throw new SqLinkException("不支持的表达式:" + methodCall);
                }
                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
                ISqlExpression column = visit(methodCall.getArgs().get(0));
                IAggregateMethods agg = config.getTransformer();
                queryable.setSelect(factory.select(Collections.singletonList(agg.avg(column)), BigDecimal.class));
                // 在终结的地方弹出
                pop();
                return queryable;
            }
            else if (methodName.equals("min"))
            {

                ISqlExpression visit = visit(methodCall.getExpr());
                if (!(visit instanceof ISqlQueryableExpression))
                {
                    throw new SqLinkException("不支持的表达式:" + methodCall);
                }
                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
                ISqlExpression column = visit(methodCall.getArgs().get(0));
                IAggregateMethods agg = config.getTransformer();
                queryable.setSelect(factory.select(Collections.singletonList(agg.min(column)), BigDecimal.class));
                // 在终结的地方弹出
                pop();
                return queryable;
            }
            else if (methodName.equals("max"))
            {

                ISqlExpression visit = visit(methodCall.getExpr());
                if (!(visit instanceof ISqlQueryableExpression))
                {
                    throw new SqLinkException("不支持的表达式:" + methodCall);
                }
                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
                ISqlExpression column = visit(methodCall.getArgs().get(0));
                IAggregateMethods agg = config.getTransformer();
                queryable.setSelect(factory.select(Collections.singletonList(agg.max(column)), BigDecimal.class));
                // 在终结的地方弹出
                pop();
                return queryable;
            }
            else if (methodName.equals("any"))
            {
                ISqlExpression visit = visit(methodCall.getExpr());
                if (!(visit instanceof ISqlQueryableExpression))
                {
                    throw new SqLinkException("不支持的表达式:" + methodCall);
                }
                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
                List<Expression> args = methodCall.getArgs();
                if (!args.isEmpty())
                {
                    Expression expression = args.get(0);
                    ISqlExpression cond = visit(expression);
                    queryable.addWhere(cond);
                }
                queryable.setSelect(factory.select(Collections.singletonList(factory.constString("1")), int.class));
                ISqlUnaryExpression any = factory.unary(SqlOperator.EXISTS, queryable);
                // 在终结的地方弹出
                pop();
                return any;
            }
            else if (methodName.equals("where"))
            {
                ISqlExpression visit = visit(methodCall.getExpr());
                if (!(visit instanceof ISqlQueryableExpression))
                {
                    throw new SqLinkException("不支持的表达式:" + methodCall);
                }
                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
                ISqlExpression cond = visit(methodCall.getArgs().get(0));
                queryable.addWhere(cond);
                return queryable;
            }
            else if (methodName.equals("select"))
            {
                ISqlExpression visit = visit(methodCall.getExpr());
                if (!(visit instanceof ISqlQueryableExpression))
                {
                    throw new SqLinkException("不支持的表达式:" + methodCall);
                }
                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
                ISqlExpression select = visit(methodCall.getArgs().get(0));
                queryable.setSelect(factory.select(Collections.singletonList(select), queryable.getMainTableClass()));
                return factory.queryable(queryable.getSelect(), factory.from(queryable, queryable.getFrom().getTableRefExpression()));
            }
            else if (methodName.equals("distinct"))
            {

                ISqlExpression visit = visit(methodCall.getExpr());
                if (!(visit instanceof ISqlQueryableExpression))
                {
                    throw new SqLinkException("不支持的表达式:" + methodCall);
                }
                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
                List<Expression> args = methodCall.getArgs();
                if (args.isEmpty())
                {
                    queryable.setDistinct(true);
                }
                else
                {
                    ISqlExpression value = visit(args.get(0));
                    if (value instanceof ISqlSingleValueExpression)
                    {
                        ISqlSingleValueExpression iSqlSingleValueExpression = (ISqlSingleValueExpression) value;
                        queryable.setDistinct((boolean) iSqlSingleValueExpression.getValue());
                    }
                    else
                    {
                        throw new SqLinkException("不支持的表达式:" + methodCall);
                    }
                }
                return queryable;
            }
            else if (methodName.equals("orderBy"))
            {

                ISqlExpression visit = visit(methodCall.getExpr());
                if (!(visit instanceof ISqlQueryableExpression))
                {
                    throw new SqLinkException("不支持的表达式:" + methodCall);
                }
                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;
                List<Expression> args = methodCall.getArgs();
                ISqlExpression orderByColumn = visit(args.get(0));

                if (args.size() > 1)
                {
                    ISqlExpression value = visit(args.get(1));
                    if (value instanceof ISqlSingleValueExpression)
                    {
                        ISqlSingleValueExpression iSqlSingleValueExpression = (ISqlSingleValueExpression) value;
                        queryable.addOrder(factory.order(orderByColumn, (boolean) iSqlSingleValueExpression.getValue()));
                    }
                    else
                    {
                        throw new SqLinkException("不支持的表达式:" + methodCall);
                    }
                }
                else
                {
                    queryable.addOrder(factory.order(orderByColumn));
                }
                return queryable;
            }
            else if (methodName.equals("leftJoin"))
            {
                throw new SqLinkException("过于复杂的表达式:" + methodCall);
            }
            else if (methodName.equals("innerJoin"))
            {
                throw new SqLinkException("过于复杂的表达式:" + methodCall);
            }
            else if (methodName.equals("rightJoin"))
            {
                throw new SqLinkException("过于复杂的表达式:" + methodCall);
            }
            else if (methodName.equals("groupBy"))
            {
                throw new SqLinkException("过于复杂的表达式:" + methodCall);
            }
            else if (methodName.equals("having"))
            {
                throw new SqLinkException("过于复杂的表达式:" + methodCall);
            }
            else if (methodName.equals("limit"))
            {

                ISqlExpression visit = visit(methodCall.getExpr());
                if (!(visit instanceof ISqlQueryableExpression))
                {
                    throw new SqLinkException("不支持的表达式:" + methodCall);
                }
                ISqlQueryableExpression queryable = (ISqlQueryableExpression) visit;

                List<Expression> args = methodCall.getArgs();
                if (args.size() == 1)
                {
                    ISqlExpression rows = visit(args.get(0));
                    if (rows instanceof ISqlSingleValueExpression)
                    {
                        ISqlSingleValueExpression iSqlSingleValueExpression = (ISqlSingleValueExpression) rows;
                        queryable.setLimit(0, (long) iSqlSingleValueExpression.getValue());
                    }
                    else
                    {
                        throw new SqLinkException("不支持的表达式:" + methodCall);
                    }
                }
                else if (args.size() == 2)
                {
                    ISqlExpression offset = visit(args.get(0));
                    ISqlExpression rows = visit(args.get(1));
                    if (rows instanceof ISqlSingleValueExpression && offset instanceof ISqlSingleValueExpression)
                    {
                        ISqlSingleValueExpression rowsValue = (ISqlSingleValueExpression) rows;
                        ISqlSingleValueExpression offsetValue = (ISqlSingleValueExpression) offset;
                        queryable.setLimit((long) offsetValue.getValue(), (long) rowsValue.getValue());
                    }
                    else
                    {
                        throw new SqLinkException("不支持的表达式:" + methodCall);
                    }
                }
                return queryable;
            }
            else if (methodName.equals("toList"))
            {
                ISqlExpression visit = visit(methodCall.getExpr());
                if (!(visit instanceof ISqlQueryableExpression))
                {
                    throw new SqLinkException("不支持的表达式:" + methodCall);
                }
                // 在终结的地方弹出
                pop();
                return visit;
            }
            else
            {
                return checkAndReturnValue(methodCall);
            }
        }
        // 集合的函数
        else if (Collection.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            Method method = methodCall.getMethod();
            if (method.getName().equals("contains"))
            {
                ISqlExpression left = visit(methodCall.getArgs().get(0));
                ISqlExpression right = visit(methodCall.getExpr());
                ISqlBinaryExpression binary = factory.binary(SqlOperator.IN, left, right);
                // 在终结的地方弹出
                pop();
                return binary;
            }
            else if (method.getName().equals("size"))
            {
                ISqlExpression left = visit(methodCall.getExpr());
                if (left instanceof ISqlQueryableExpression)
                {
                    ISqlQueryableExpression query = (ISqlQueryableExpression) left;
                    IAggregateMethods agg = config.getTransformer();
                    query.setSelect(factory.select(Collections.singletonList(agg.count()), long.class));
                    // 在终结的地方弹出
                    pop();
                    return query;
                }
                else
                {
                    throw new DrinkException(String.format("意外的sql表达式类型:%s 表达式为:%s", left.getClass(), methodCall));
                }
            }
            else if (method.getName().equals("isEmpty"))
            {
                ISqlExpression left = visit(methodCall.getExpr());
                if (left instanceof ISqlQueryableExpression)
                {
                    ISqlQueryableExpression query = (ISqlQueryableExpression) left;
                    query.setSelect(factory.select(Collections.singletonList(factory.constString("1")), int.class));
                    ISqlUnaryExpression not = factory.unary(SqlOperator.NOT, factory.unary(SqlOperator.EXISTS, query));
                    // 在终结的地方弹出
                    pop();
                    return not;
                }
                else
                {
                    throw new SqLinkException(String.format("意外的sql表达式类型:%s 表达式为:%s", left.getClass(), methodCall));
                }
            }
//            else if (method.getName().equals("stream")) {
//                流支持的功能太少了，就不写了
//            }
            else
            {
                return checkAndReturnValue(methodCall);
            }
        }
        // 字符串的函数
        else if (String.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            Method method = methodCall.getMethod();
            IStringMethods str = config.getTransformer();
            if (Modifier.isStatic(method.getModifiers()))
            {
                switch (method.getName())
                {
                    case "join":
                    {
                        ISqlExpression delimiter = visit(methodCall.getArgs().get(0));
                        //String.join(CharSequence delimiter, CharSequence... elements)
                        if (method.isVarArgs())
                        {
                            List<ISqlExpression> args = new ArrayList<>(methodCall.getArgs().size() - 1);
                            for (int i = 1; i < methodCall.getArgs().size(); i++)
                            {
                                args.add(visit(methodCall.getArgs().get(i)));
                            }
                            return str.joinArray(delimiter, args);
                        }
                        else
                        {
                            ISqlExpression elements = visit(methodCall.getArgs().get(1));
                            return str.joinList(delimiter, elements);
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
                        ISqlExpression left = visit(methodCall.getExpr());
                        ISqlExpression right = visit(methodCall.getArgs().get(0));
                        return str.contains(left, right);
                    }
                    case "startsWith":
                    {
                        ISqlExpression left = visit(methodCall.getExpr());
                        ISqlExpression right = visit(methodCall.getArgs().get(0));
                        return str.startsWith(left, right);
                    }
                    case "endsWith":
                    {
                        ISqlExpression left = visit(methodCall.getExpr());
                        ISqlExpression right = visit(methodCall.getArgs().get(0));
                        return str.endsWith(left, right);
                    }
                    case "length":
                    {
                        ISqlExpression left = visit(methodCall.getExpr());
                        return str.length(left);
                    }
                    case "toUpperCase":
                    {
                        ISqlExpression left = visit(methodCall.getExpr());
                        return str.toUpperCase(left);
                    }
                    case "toLowerCase":
                    {
                        ISqlExpression left = visit(methodCall.getExpr());
                        return str.toLowerCase(left);
                    }
                    case "concat":
                    {
                        ISqlExpression left = visit(methodCall.getExpr());
                        ISqlExpression right = visit(methodCall.getArgs().get(0));
                        return str.concat(left, right);
                    }
                    case "trim":
                    {
                        ISqlExpression left = visit(methodCall.getExpr());
                        return str.trim(left);
                    }
                    case "isEmpty":
                    {
                        ISqlExpression left = visit(methodCall.getExpr());
                        return str.isEmpty(left);
                    }
                    case "indexOf":
                    {
                        if (method.getParameterTypes()[0] == String.class)
                        {
                            ISqlExpression thisStr = visit(methodCall.getExpr());
                            ISqlExpression subStr = visit(methodCall.getArgs().get(0));
                            if (method.getParameterCount() == 1)
                            {
                                return str.indexOf(thisStr, subStr);
                            }
                            else
                            {
                                ISqlExpression fromIndex = visit(methodCall.getArgs().get(1));
                                return str.indexOf(thisStr, subStr, fromIndex);
                            }
                        }
                    }
                    case "replace":
                    {
                        ISqlExpression thisStr = visit(methodCall.getExpr());
                        ISqlExpression oldStr = visit(methodCall.getArgs().get(0));
                        ISqlExpression newStr = visit(methodCall.getArgs().get(1));
                        return str.replace(thisStr, oldStr, newStr);
                    }
                    case "substring":
                    {
                        ISqlExpression thisStr = visit(methodCall.getExpr());
                        ISqlExpression beginIndex = visit(methodCall.getArgs().get(0));
                        if (method.getParameterCount() == 1)
                        {
                            return str.substring(thisStr, beginIndex);
                        }
                        else
                        {
                            ISqlExpression endIndex = visit(methodCall.getArgs().get(1));
                            return str.substring(thisStr, beginIndex, endIndex);
                        }
                    }
                    default:
                        return checkAndReturnValue(methodCall);
                }
            }
        }
        // Math的函数
        else if (Math.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            Method method = methodCall.getMethod();
            IMathMethods math = config.getTransformer();
            switch (method.getName())
            {
                case "abs":
                {
                    ISqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("ABS(", ")"), Collections.singletonList(arg));
                }
                case "cos":
                {
                    ISqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("COS(", ")"), Collections.singletonList(arg));
                }
                case "acos":
                {
                    ISqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("ACOS(", ")"), Collections.singletonList(arg));
                }
                case "sin":
                {
                    ISqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("SIN(", ")"), Collections.singletonList(arg));
                }
                case "asin":
                {
                    ISqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("ASIN(", ")"), Collections.singletonList(arg));
                }
                case "tan":
                {
                    ISqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("TAN(", ")"), Collections.singletonList(arg));
                }
                case "atan":
                {
                    ISqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("ATAN(", ")"), Collections.singletonList(arg));
                }
                case "atan2":
                {
                    ISqlExpression arg1 = visit(methodCall.getArgs().get(0));
                    ISqlExpression arg2 = visit(methodCall.getArgs().get(1));
                    return math.atan2(arg1, arg2);
                }
                case "toDegrees":
                {
                    ISqlExpression arg = visit(methodCall.getArgs().get(0));
                    return math.toDegrees(arg);
                }
                case "toRadians":
                {
                    ISqlExpression arg = visit(methodCall.getArgs().get(0));
                    return math.toRadians(arg);
                }
                case "exp":
                {
                    ISqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("EXP(", ")"), Collections.singletonList(arg));
                }
                case "floor":
                {
                    ISqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("FLOOR(", ")"), Collections.singletonList(arg));
                }
                case "log":
                {
                    ISqlExpression arg = visit(methodCall.getArgs().get(0));
                    return math.log(arg);
                }
                case "log10":
                {
                    ISqlExpression arg = visit(methodCall.getArgs().get(0));
                    return math.log10(arg);
                }
                case "random":
                {
                    return math.random();
                }
                case "round":
                {
                    ISqlExpression arg = visit(methodCall.getArgs().get(0));
                    return math.round(arg);
                }
                case "pow":
                {
                    ISqlExpression arg1 = visit(methodCall.getArgs().get(0));
                    ISqlExpression arg2 = visit(methodCall.getArgs().get(1));
                    return factory.template(Arrays.asList("POWER(", ",", ")"), Arrays.asList(arg1, arg2));
                }
                case "signum":
                {
                    ISqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("SIGN(", ")"), Collections.singletonList(arg));
                }
                case "sqrt":
                {
                    ISqlExpression arg = visit(methodCall.getArgs().get(0));
                    return factory.template(Arrays.asList("SQRT(", ")"), Collections.singletonList(arg));
                }
                default:
                    return checkAndReturnValue(methodCall);
            }
        }
        // BigDecimal||BigInteger的函数
        else if (BigDecimal.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass())
                || BigInteger.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            Method method = methodCall.getMethod();
            INumberMethods number = config.getTransformer();
            switch (method.getName())
            {
                case "add":
                {
                    if (method.getParameterCount() == 1)
                    {
                        ISqlExpression left = visit(methodCall.getExpr());
                        ISqlExpression right = visit(methodCall.getArgs().get(0));
                        return factory.binary(SqlOperator.PLUS, left, right);
                    }
                }
                case "subtract":
                {
                    if (method.getParameterCount() == 1)
                    {
                        ISqlExpression left = visit(methodCall.getExpr());
                        ISqlExpression right = visit(methodCall.getArgs().get(0));
                        return factory.binary(SqlOperator.MINUS, left, right);
                    }
                }
                case "multiply":
                {
                    if (method.getParameterCount() == 1)
                    {
                        ISqlExpression left = visit(methodCall.getExpr());
                        ISqlExpression right = visit(methodCall.getArgs().get(0));
                        return factory.binary(SqlOperator.MUL, left, right);
                    }
                }
                case "divide":
                {
                    if (method.getParameterCount() == 1)
                    {
                        ISqlExpression left = visit(methodCall.getExpr());
                        ISqlExpression right = visit(methodCall.getArgs().get(0));
                        return factory.binary(SqlOperator.DIV, left, right);
                    }
                }
                case "remainder":
                {
                    if (method.getParameterCount() == 1)
                    {
                        ISqlExpression left = visit(methodCall.getExpr());
                        ISqlExpression right = visit(methodCall.getArgs().get(0));
                        return number.remainder(left, right);
                    }
                }
                default:
                    return checkAndReturnValue(methodCall);
            }
        }
        // 时间的函数
        else if (Temporal.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            Method method = methodCall.getMethod();
            ITimeMethods time = config.getTransformer();
            switch (method.getName())
            {
                case "isAfter":
                {
                    ISqlExpression left = visit(methodCall.getExpr());
                    ISqlExpression right = visit(methodCall.getArgs().get(0));
                    return time.isAfter(left, right);
                }
                case "isBefore":
                {
                    ISqlExpression left = visit(methodCall.getExpr());
                    ISqlExpression right = visit(methodCall.getArgs().get(0));
                    return time.isBefore(left, right);
                }
                case "isEqual":
                {
                    ISqlExpression left = visit(methodCall.getExpr());
                    ISqlExpression right = visit(methodCall.getArgs().get(0));
                    return time.isEqual(left, right);
                }
                default:
                    return checkAndReturnValue(methodCall);
            }
        }
        // Objects的函数
        else if (Objects.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            Method method = methodCall.getMethod();
            IObjectsMethods objects = config.getTransformer();
            if (method.getName().equals("equals"))
            {
                List<Expression> args = methodCall.getArgs();
                return factory.binary(SqlOperator.EQ, visit(args.get(0)), visit(args.get(1)));
            }
            else if (method.getName().equals("nonNull"))
            {
                return objects.notNull(visit(methodCall.getArgs().get(0)));
            }
            else
            {
                return checkAndReturnValue(methodCall);
            }
        }
        else
        {
//            if (isProperty(asNameMap, methodCall)) {
//                if (isGetter(methodCall.getMethod())) {
//                    ParameterExpression parameter = (ParameterExpression) methodCall.getExpr();
//                    Method getter = methodCall.getMethod();
//                    MetaData metaData = config.getMetaData(getter.getDeclaringClass());
//                    ISqlTableRefExpression asName = getISqlTableRefExpressionByIndex(parameter);
//                    return factory.column(metaData.getFieldMetaDataByGetter(getter), asName);
//                }
//                else if (isGroupValue(asNameMap, methodCall.getExpr())) // g.value?.field
//                {
//                    FieldSelectExpression expr = (FieldSelectExpression) methodCall.getExpr();
//                    String vname = expr.getField().getName();
//                    int valueIndex = Integer.parseInt(vname.replace("value", ""));
//                    List<ISqlTableRefExpression> asNameList = asNameListDeque.peek();
//                    ISqlTableRefExpression asName = asNameList.get(valueIndex);
//                    Method getter = methodCall.getMethod();
//                    MetaData metaData = config.getMetaData(getter.getDeclaringClass());
//                    return factory.column(metaData.getFieldMetaDataByGetter(getter), asName);
//                }
//                else if (isSetter(methodCall.getMethod())) {
//                    ParameterExpression parameter = (ParameterExpression) methodCall.getExpr();
//                    Method setter = methodCall.getMethod();
//                    MetaData metaData = config.getMetaData(setter.getDeclaringClass());
//                    FieldMetaData fieldMetaData = metaData.getFieldMetaDataBySetter(setter);
//                    ISqlColumnExpression columnExpression = factory.column(fieldMetaData, asNameMap.get(parameter));
//                    ISqlExpression value = visit(methodCall.getArgs().get(0));
//                    return factory.set(columnExpression, value);
//                }
//                else if (isDynamicColumn(methodCall.getMethod())) {
//                    ParameterExpression parameter = (ParameterExpression) methodCall.getExpr();
//                    List<Expression> args = methodCall.getArgs();
//                    Expression expression = args.get(0);
//                    String columnName = expression.getValue().toString();
//                    Expression expression1 = args.get(1);
//                    Class<?> value = (Class<?>) expression1.getValue();
//                    ISqlTableRefExpression asName = getISqlTableRefExpressionByIndex(parameter);
//                    return factory.dynamicColumn(columnName, value, asName);
//                }
//                else {
//                    return checkAndReturnValue(methodCall);
//                }
//            }
//            else {
//                ISqlExpression left = visit(methodCall.getExpr());
//                // if left is A.B()
//                if (left instanceof ISqlColumnExpression) {
//                    ISqlColumnExpression columnExpression = (ISqlColumnExpression) left;
//                    Method method = methodCall.getMethod();
//                    if (isGetter(method)) {
//                        // A.B() => SELECT ... FROM B WHERE B.ID = A.ID
//                        // ISqlQueryableExpression changed = columnToQuery(columnExpression);
//                        // A.B().C() => SELECT ... FROM C WHERE C.ID = (SELECT B.ID FROM B WHERE B.ID = A.ID)
//                        //  return queryToQuery(changed, method);
//                    }
//                    else {
//                        return checkAndReturnValue(methodCall);
//                    }
//                }
//                // if left is A.B()...N()
//                else if (left instanceof ISqlQueryableExpression) {
//                    Method method = methodCall.getMethod();
//                    if (isGetter(method)) {
//                        ISqlQueryableExpression queryableExpression = (ISqlQueryableExpression) left;
//                        return queryToQuery(queryableExpression, method);
//                    }
//                    else {
//                        return checkAndReturnValue(methodCall);
//                    }
//                }
//                else {
//                    return checkAndReturnValue(methodCall);
//                }
//            }
            Expression expression = methodCall.getExpr();
            Method method = methodCall.getMethod();
            ISqlExpression left = visit(expression);
            // (a) -> a
            if (left instanceof ISqlTableRefExpression)
            {
                ISqlTableRefExpression tableRef = (ISqlTableRefExpression) left;
                // a.getter()
                if (isGetter(method))
                {
                    FieldMetaData getter = methodToFieldMetaData(method,tableRef);
                    // 如果这个getter是导航属性
                    // 那么他应该被映射成sql语句
                    if (getter.hasNavigate())
                    {
                        return getterToSqlAst(getter, tableRef);
                    }
                    else
                    {
                        return factory.column(getter, tableRef);
                    }
                }
                else if (isDynamicColumn(method))
                {
                    List<Expression> args = methodCall.getArgs();
                    String column = args.get(0).getValue().toString();
                    Class<?> type = (Class<?>) args.get(1).getValue();
                    return factory.dynamicColumn(column,type, tableRef);
                }
                else
                {
                    return checkAndReturnValue(methodCall);
                }
            }
            // ?.table()
            else if (left instanceof ISqlQueryableExpression)
            {
                ISqlQueryableExpression leftQuery = (ISqlQueryableExpression) left;
                // ?.table().getter()
                if (isGetter(method))
                {
                    MetaData metaData = config.getMetaData(method.getDeclaringClass());
                    FieldMetaData getter = metaData.getFieldMetaDataByGetter(method);
                    if (getter.hasNavigate())
                    {
                        ISqlQueryableExpression query = queryToQueryable(leftQuery, getter);
                        // 弹出旧的
                        pop();
                        //  在子查询发起的地方压入
                        push(query);
                        return query;
                    }
                    else
                    {
                        // select ? from table ...
                        // select table.getter from table ...
                        leftQuery.setSelect(factory.select(Collections.singletonList(factory.column(getter, leftQuery.getFrom().getTableRefExpression())), getter.getType()));
                        return leftQuery;
                    }
                }
                else if (isDynamicColumn(method))
                {
                    List<Expression> args = methodCall.getArgs();
                    String column = args.get(0).getValue().toString();
                    Class<?> type = (Class<?>) args.get(1).getValue();
                    leftQuery.setSelect(factory.select(Collections.singletonList(factory.dynamicColumn(column,type, leftQuery.getFrom().getTableRefExpression())), type));
                    return leftQuery;
                }
                else
                {
                    throw new DrinkException(methodCall.toString());
                }
            }
            else
            {
                return checkAndReturnValue(methodCall);
            }
        }
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
        ILogic logic = config.getTransformer();
        return logic.If(cond, truePart, falsePart);
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
                List<ISqlExpression> expressions = new ArrayList<>();
                for (Expression expression : classBody.getExpressions()) {
                    if (expression.getKind() == Kind.Variable) {
                        VariableExpression variable = (VariableExpression) expression;
                        String name = variable.getName();
                        Expression init = variable.getInit();
                        if (init != null) {
                            ISqlExpression context = visit(variable.getInit());
                            // 某些数据库不支持直接返回bool类型，所以需要做一下包装
                            context = boxTheBool(variable.getInit(), context);
                            setAs(expressions, context, name);
                        }
                    }
                }
                return factory.select(expressions, newExpression.getType());
            }
            else {
                return checkAndReturnValue(newExpression);
            }
        }
    }

    @Override
    public ISqlExpression visit(ParameterExpression parameter) {
        if (IGroup.class.isAssignableFrom(parameter.getType())) {
            return factory.groupRef();
        }
        else {
            return getISqlTableRefExpressionByIndex(parameter);
        }
    }

    @Override
    public ISqlExpression visit(BlockExpression blockExpression) {
        List<ISqlSetExpression> sqlSetExpressions = new ArrayList<>();
        for (Expression expression : blockExpression.getExpressions()) {
            ISqlExpression visit = visit(expression);
            if (visit instanceof ISqlSetExpression) {
                sqlSetExpressions.add((ISqlSetExpression) visit);
            }
            else {
                throw new SqLinkException(String.format("意外的sql表达式类型:%s 表达式为:%s", visit.getClass(), expression));
            }
        }
        ISqlSetsExpression sets = factory.sets();
        sets.addSet(sqlSetExpressions);
        return sets;
    }

    @Override
    public ISqlExpression visit(TypeCastExpression typeCast) {
        return factory.typeCast(typeCast.getTargetType(), visit(typeCast.getExpr()));
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
        Optional<SqlExtensionExpression> first = Arrays.stream(sqlExtensionExpressions).filter(a -> a.dbType() == dbType).findFirst();
        if (!first.isPresent()) {
            Optional<SqlExtensionExpression> any = Arrays.stream(sqlExtensionExpressions).filter(a -> a.dbType() == DbType.Any).findFirst();
            if (any.isPresent()) {
                return any.get();
            }
            throw new SqlFuncExtNotFoundException(dbType);
        }
        else {
            return first.get();
        }
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
        List<ISqlTableRefExpression> asNameList = asNameListDeque.peek();

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

    protected ISqlExpression boxTheBool(Expression init, ISqlExpression result) {
        if (init instanceof MethodCallExpression) {
            MethodCallExpression methodCall = (MethodCallExpression) init;
            return boxTheBool(isBool(methodCall.getMethod().getReturnType()), result);
        }
        else if (init instanceof UnaryExpression) {
            UnaryExpression unary = (UnaryExpression) init;
            return boxTheBool(unary.getOperatorType() == OperatorType.NOT, result);
        }
        return result;
    }

    protected ISqlExpression boxTheBool(boolean condition, ISqlExpression result) {
        if (!condition) return result;
        switch (config.getDbType()) {
            case SQLServer:
            case Oracle:
                ILogic logic = config.getTransformer();
                return logic.If(result, factory.constString("1"), factory.constString("0"));
            default:
                return result;
        }
    }

    public ISqlSelectExpression toSelect(LambdaExpression<?> lambda, ISqlQueryableExpression queryable) {
        ISqlExpression expression = visit(lambda);
        ISqlSelectExpression selectExpression;
        if (expression instanceof ISqlSelectExpression) {
            selectExpression = (ISqlSelectExpression) expression;
        }
        else if (expression instanceof ISqlTableRefExpression) {
            ISqlTableRefExpression tableRef = (ISqlTableRefExpression) expression;
            List<ISqlTableRefExpression> peek = asNameListDeque.peek();
            int index = peek.indexOf(tableRef);
            Class<?> tableClass;
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
        else if (expression instanceof ISqlGroupRef) {
            throw new DrinkException("ISqlGroupRef");
        }
        else {
            SqlExpressionFactory factory = config.getSqlExpressionFactory();
            // 用于包装某些数据库不支持直接返回bool
            if (isBool(lambda.getReturnType()))
            {
                switch (config.getDbType())
                {
                    case SQLServer:
                    case Oracle:
                        ILogic logic = config.getTransformer();
                        expression = logic.If(expression, factory.constString("1"), factory.constString("0"));
                }
            }
            selectExpression = factory.select(Collections.singletonList(expression), lambda.getReturnType(), true, false);
        }
        return selectExpression;
    }

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
        if (body instanceof FieldSelectExpression)
        {
            FieldSelectExpression fieldSelect = (FieldSelectExpression) body;
            if (fieldSelect.getExpr() instanceof ParameterExpression)
            {
                ParameterExpression expr = (ParameterExpression) fieldSelect.getExpr();
                return config.getMetaData(expr.getType()).getFieldMetaDataByFieldName(fieldSelect.getField().getName());
            }
        }
        else if (body instanceof MethodCallExpression)
        {
            MethodCallExpression methodCall = (MethodCallExpression) body;
            if (methodCall.getExpr() instanceof ParameterExpression)
            {
                ParameterExpression expr = (ParameterExpression) methodCall.getExpr();
                return config.getMetaData(expr.getType()).getFieldMetaDataByGetter(methodCall.getMethod());
            }
        }
        throw new DrinkException(lambda.toString());
    }

    protected ISqlTableRefExpression getISqlTableRefExpressionByIndex(ParameterExpression parameter) {
        ISqlTableRefExpression asName;
        if (index >= 0) {
            asName = asNameListDeque.peek().get(index);
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

    protected FieldMetaData methodToFieldMetaData(Method method,ISqlTableRefExpression tableRef)
    {
        Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass.isInterface())
        {
            List<ISqlTableRefExpression> peek = asNameListDeque.peek();
            int index = peek.indexOf(tableRef);
            Class<?> tableClass;
            if (index == 0) {
                tableClass = fromDeque.peek().getType();
            }
            else {
                tableClass = joinsDeque.peek().getJoins().get(index-1).getJoinTable().getType();
            }
            MetaData metaData = config.getMetaData(tableClass);
            return metaData.getFieldMetaDataByGetterName(method.getName());
        }
        else
        {
            MetaData metaData = config.getMetaData(declaringClass);
            return metaData.getFieldMetaDataByGetter(method);
        }
    }
}
