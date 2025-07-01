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

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.metaData.*;
import io.github.kiryu1223.drink.base.sqlExt.BaseSqlExtension;
import io.github.kiryu1223.drink.base.sqlExt.SqlExtensionExpression;
import io.github.kiryu1223.drink.base.sqlExt.SqlOperatorMethod;
import io.github.kiryu1223.drink.base.transform.*;
import io.github.kiryu1223.drink.base.util.DrinkUtil;
import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.drink.core.api.crud.read.Aggregate;
import io.github.kiryu1223.drink.core.api.crud.read.QueryBase;
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

/**
 * 表达式解析器
 *
 * @author kiryu1223
 * @since 3.0
 */
public class UpdateSqlVisitor extends BaseSqlVisitor {
    protected int index = -1;
    protected final Deque<List<ISqlTableRefExpression>> asNameListDeque = new ArrayDeque<>();
    protected final Map<ParameterExpression, ISqlTableRefExpression> asNameMap = new HashMap<>();
    protected final Deque<ISqlFromExpression> fromDeque = new ArrayDeque<>();
    protected final Deque<ISqlJoinsExpression> joinsDeque = new ArrayDeque<>();

    public UpdateSqlVisitor(IConfig config, ISqlUpdateExpression updateExpression) {
        this(config, -1);
        push(updateExpression);
    }

    public UpdateSqlVisitor(IConfig config, ISqlUpdateExpression updateExpression,int index) {
        this(config, index);
        push(updateExpression);
    }

    public UpdateSqlVisitor(IConfig config, ISqlDeleteExpression deleteExpression) {
        this(config, -1);
        push(deleteExpression);
    }

    public UpdateSqlVisitor(IConfig config, ISqlDeleteExpression deleteExpression,int index) {
        this(config, index);
        push(deleteExpression);
    }

    protected UpdateSqlVisitor(IConfig config, int index) {
        super(config);
        this.index = index;
    }

    protected void push(ISqlUpdateExpression updateExpression) {
        ISqlFromExpression from = updateExpression.getFrom();
        ISqlJoinsExpression joins = updateExpression.getJoins();
        List<ISqlTableRefExpression> asNameList = new ArrayList<>();
        asNameList.add(from.getTableRefExpression());
        for (ISqlJoinExpression join : joins.getJoins()) {
            asNameList.add(join.getTableRefExpression());
        }
        push(asNameList, from, joins);
    }

    protected void push(ISqlDeleteExpression deleteExpression) {
        ISqlFromExpression from = deleteExpression.getFrom();
        ISqlJoinsExpression joins = deleteExpression.getJoins();
        List<ISqlTableRefExpression> asNameList = new ArrayList<>();
        asNameList.add(from.getTableRefExpression());
        for (ISqlJoinExpression join : joins.getJoins()) {
            asNameList.add(join.getTableRefExpression());
        }
        push(asNameList, from, joins);
    }

    protected void push(List<ISqlTableRefExpression> tableRefExpressions, ISqlFromExpression fromExpression, ISqlJoinsExpression joinsExpression) {
        asNameListDeque.push(tableRefExpressions);
        fromDeque.push(fromExpression);
        joinsDeque.push(joinsExpression);
    }

    protected void pop() {
        asNameListDeque.pop();
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
        if (left instanceof ISqlTableRefExpression) {
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
        return checkAndReturnValue(newExpression);
    }

    @Override
    public ISqlExpression visit(ParameterExpression parameter) {
        return getISqlTableRefExpressionByIndex(parameter);
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
        return getterToQueryable(getter, tableRef);
    }

    protected FieldMetaData methodToFieldMetaData(Method method, ISqlTableRefExpression tableRef) {
        Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass.isInterface()) {
            List<ISqlTableRefExpression> peek = asNameListDeque.peek();
            int index = peek.indexOf(tableRef);
            Class<?> tableClass;
            if (index == 0) {
                tableClass = fromDeque.peek().getType();
            }
            else {
                tableClass = joinsDeque.peek().getJoins().get(index - 1).getJoinTable().getType();
            }
            MetaData metaData = config.getMetaData(tableClass);
            return metaData.getFieldMetaDataByGetterName(method.getName());
        }
        else {
            MetaData metaData = config.getMetaData(declaringClass);
            return metaData.getFieldMetaDataByGetter(method);
        }
    }

    protected void parse$(Expression arg, List<String> strings)
    {
        ISqlExpression visit = visit(arg);
        if (visit instanceof ISqlSingleValueExpression)
        {
            ISqlSingleValueExpression singleValue = (ISqlSingleValueExpression) visit;
            strings.add(singleValue.getValue().toString());
        }
        else if (visit instanceof ISqlCollectedValueExpression)
        {
            ISqlCollectedValueExpression collectedValue = (ISqlCollectedValueExpression) visit;
            strings.add(collectedValue.getCollection().toString());
        }
        else
        {
            throw new DrinkException("raw参数必须是求值的");
        }
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
}
