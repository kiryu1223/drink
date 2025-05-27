package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.session.SqlSession;

import java.util.*;

public class SubQueryBuilder {
    private final IConfig config;
    private final SqlExpressionFactory factory;
    // 需要被注入的字段
    private final FieldMetaData fieldMetaData;
    // 子查询
    private final ISqlQueryableExpression queryableExpression;

    public SubQueryBuilder(IConfig config, FieldMetaData fieldMetaData, ISqlQueryableExpression queryableExpression) {
        this.config = config;
        this.fieldMetaData = fieldMetaData;
        this.queryableExpression = queryableExpression;
        factory = config.getSqlExpressionFactory();
    }

    static class Values {
        private final int level;
        // key = value:xxx
        private final Map<String, Value> map = new HashMap<>();
        private int count;
        private int index = -1;

        Values(int level) {
            this.level = level;
        }

        private boolean next() {
            return ++index < count;
        }

        private void reset() {
            index = -1;
        }

        private Object getValue(String valueName) {
            return map.get(valueName).getValue(index);
        }
    }

    static class Value {
        private final FieldMetaData fieldMetaData;
        private final List<Object> values = new ArrayList<>();

        Value(FieldMetaData fieldMetaData) {
            this.fieldMetaData = fieldMetaData;
        }

        private Object getValue(int index) {
            return values.get(index);
        }
    }

    public void subQuery(
            // 会话
            SqlSession session,
            // 原始集合
            Collection<?> sources,
            // 上下文参数
            // select *,? as `value:id`,? as `value:name`
            List<ExValues> valueContext
    ) {
        // 0,1,2,3,4,5,6,7
        List<Values> valuesList = new ArrayList<>();
        // 因为可能需要的参数不是连续的
        // 0,1,3,7
        Map<Integer, Values> valueMap = new HashMap<>();

        queryableExpression.accept(new SqlTreeVisitor() {
            @Override
            public void visit(ISqlColumnExpression expr) {
                super.visit(expr);
                if (expr instanceof SubQueryValue) {
                    SubQueryValue subQueryValue = (SubQueryValue) expr;
                    String valueName = subQueryValue.getValueName();
                    int level = subQueryValue.getLevel();
                    Values values = valueMap.get(level);
                    if (values == null) {
                        values = new Values(level);
                        valueMap.put(level, values);
                        valuesList.add(values);
                    }
                    if (!values.map.containsKey(valueName)) {
                        ExValues exValues = valueContext.get(level);
                        ExValue exValue = exValues.getExValueMap().get(valueName);
                        Value value = new Value(exValue.getFieldMetaData());
                        value.values.addAll(exValue.getValues());
                        values.map.put(valueName, value);
                        values.count = exValue.getValues().size();
                    }
                }
            }
        });

        if (!valuesList.isEmpty()) {
            ISqlSelectExpression select = queryableExpression.getSelect();
            for (Values values : valuesList) {
                values.map.forEach((valueName, value) -> {
                    select.addColumn(factory.as(new SubQueryValue(value.fieldMetaData, valueName, values.level), valueName));
                });
            }

            // 按层级排序
            valuesList.sort(Comparator.comparingInt(o -> o.level));
            List<ISqlQueryableExpression> list = new ArrayList<>();
            while (next(valuesList)) {
                ISqlQueryableExpression copy = queryableExpression.copy(config);
                copy.accept(new SqlTreeVisitor() {
                    @Override
                    public void visit(ISqlColumnExpression expr) {
                        super.visit(expr);
                        if (expr instanceof SubQueryValue) {
                            SubQueryValue subQueryValue = (SubQueryValue) expr;
                            String valueName = subQueryValue.getValueName();
                            int level = subQueryValue.getLevel();
                            Object value = valueMap.get(level).getValue(valueName);
                            subQueryValue.setValue(value);
                        }
                    }
                });
                list.add(copy);
            }

            ISqlUnionQueryableExpression iSqlUnionQueryableExpression = factory.unionQueryable(list, false);
            AsNameManager.start();
            String sql = iSqlUnionQueryableExpression.getSql(config);
            AsNameManager.clear();
            System.out.println(sql);
        }
        // 参数为空直接查询
        else {
            AsNameManager.start();
            System.out.println(queryableExpression.getSql(config));
            AsNameManager.clear();
        }
    }

    private boolean next(List<Values> valuesList) {
        return next(valuesList, valuesList.size() - 1);
    }

    private boolean next(List<Values> valuesList, int index) {
        if (index < 0 || index >= valuesList.size()) {
            return false;
        }
        Values values = valuesList.get(index);
        if (values.next()) {
            return true;
        }
        else {
            values.reset();
            return next(valuesList, index - 1);
        }
    }
}
