package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.session.SqlSession;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.beancreator.AbsBeanCreator;
import io.github.kiryu1223.drink.base.toBean.beancreator.ISetterCaller;
import io.github.kiryu1223.drink.base.toBean.build.ExtensionField;
import io.github.kiryu1223.drink.base.toBean.build.ExtensionObject;
import io.github.kiryu1223.drink.base.toBean.build.JdbcResult;
import io.github.kiryu1223.drink.base.toBean.build.ObjectBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.cast;

public class SubQueryBuilder {
    private final IConfig config;
    private final SqlExpressionFactory factory;
    // 需要被注入的字段
    private final FieldMetaData fieldMetaData;
    // 子查询
    private ISqlQueryableExpression queryableExpression;

    public SubQueryBuilder(IConfig config, FieldMetaData fieldMetaData, ISqlQueryableExpression queryableExpression) {
        this.config = config;
        this.fieldMetaData = fieldMetaData;
        this.queryableExpression = queryableExpression;
        factory = config.getSqlExpressionFactory();
    }

    static class Values {
        private final int level;
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

        private void addValue(String valueName, Value value) {
            map.put(valueName, value);
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

        private void addValue(Object value) {
            values.add(value);
        }
    }

    public void subQuery(
            // 会话
            SqlSession session,
            // 原始集合
            List<JdbcResult<?>> jdbcResultList
            // 上下文参数
            // select *,? as `value:id`,? as `value:name`
    ) throws InvocationTargetException, IllegalAccessException {

        Map<Integer, Values> valuesMap = new HashMap<>();
        queryableExpression.accept(new SqlTreeVisitor() {
            @Override
            public void visit(ISqlSingleValueExpression expr) {
                if (expr instanceof SubQueryValue) {
                    SubQueryValue subQueryValue = (SubQueryValue) expr;
                    int level = subQueryValue.getLevel();
                    // 层级匹配
                    JdbcResult<?> jdbcResult = jdbcResultList.get(level);
                    String valueName = subQueryValue.getValueName();
                    Values values = valuesMap.computeIfAbsent(level, k -> new Values(k));
                    Value value = new Value(subQueryValue.getFieldMetaData());
                    for (ExtensionObject<?> extensionObject : jdbcResult.getExtensionResult()) {
                        ExtensionField extensionField = extensionObject.getExtensionValueFieldMap().get(valueName);
                        value.addValue(extensionField.getValue());
                    }
                    values.addValue(valueName, value);
                }
            }
        });

        List<Values> sorted = valuesMap.values().stream()
                .sorted(Comparator.comparingInt(v -> v.level))
                .collect(Collectors.toList());

        ISqlSelectExpression select = queryableExpression.getSelect();
        ExValues exValues = select.getExValues();
        for (Values values : sorted) {
            values.map.forEach((valueName, value) -> {
                SubQueryValue sq = new SubQueryValue(value.fieldMetaData, values.level);
                // 拼到select里
                select.addColumn(factory.as(sq, sq.getValueName()));
                // 添加到exValues的额外key里
                exValues.addExKey(sq.getValueName(),value.fieldMetaData);
            });
        }

        List<ISqlQueryableExpression> list = new ArrayList<>();
        SqlTreeVisitor visitor = new SqlTreeVisitor() {
            @Override
            public void visit(ISqlSingleValueExpression expr) {
                super.visit(expr);
                if (expr instanceof SubQueryValue) {
                    SubQueryValue subQueryValue = (SubQueryValue) expr;
                    String valueName = subQueryValue.getValueName();
                    int level = subQueryValue.getLevel();
                    Object value = sorted.stream().filter(v -> v.level == level).findAny().get().getValue(valueName);
                    subQueryValue.setValue(value);
                }
            }
        };
        while (next(sorted)) {
            ISqlQueryableExpression copy = queryableExpression.copy(config);
            copy.accept(visitor);
            list.add(copy);
        }

        ISqlUnionQueryableExpression iSqlUnionQueryableExpression = factory.unionQueryable(list, false);
        List<SqlValue> sqlValues = new ArrayList<>();
        String sql = iSqlUnionQueryableExpression.getSqlAndValue(config, sqlValues);
        JdbcResult<?> result = session.executeQuery(
                resultSet -> ObjectBuilder.start(
                        resultSet,
                        iSqlUnionQueryableExpression.getMainTableClass(),
                        queryableExpression.getMappingData(config),
                        false,
                        config
                ).createList(exValues),
                sql,
                sqlValues
        );

        AbsBeanCreator<?> absBeanCreator = config.getBeanCreatorFactory().get(fieldMetaData.getType(), config);
        ISetterCaller<?> beanSetter = absBeanCreator.getBeanSetter(fieldMetaData.getFieldName());

        JdbcResult<?> currentResult = jdbcResultList.get(jdbcResultList.size() - 1);
        for (ExtensionObject<?> extensionObject : currentResult.getExtensionResult()) {
            Map<String, ExtensionField> valueFieldMap = extensionObject.getExtensionValueFieldMap();
            for (ExtensionObject<?> eob : result.getExtensionResult()) {
                Map<String, ExtensionField> keyFieldMap = eob.getExtensionKeyFieldMap();
                if (compareCommonValues(keyFieldMap, valueFieldMap)) {
                    setValue(extensionObject.getObject(),beanSetter, eob.getObject());
                }
            }
        }



        jdbcResultList.remove(jdbcResultList.size() - 1);
    }

    public static boolean compareCommonValues(Map<String, ExtensionField> smallMap, Map<String, ExtensionField> bigMap) {
        return smallMap.entrySet().stream()
                .filter(entry -> bigMap.containsKey(entry.getKey())) // 只过滤大Map存在的Key
                .allMatch(entry -> Objects.equals(entry.getValue().getValue(), bigMap.get(entry.getKey()).getValue()));
    }

    private void setValue(Object object,ISetterCaller<?> beanSetter,Object value) throws InvocationTargetException, IllegalAccessException {
        beanSetter.call(cast(object), value);
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

    public ISqlQueryableExpression getQueryableExpression() {
        return queryableExpression;
    }

    public void setQueryableExpression(ISqlQueryableExpression queryableExpression) {
        this.queryableExpression = queryableExpression;
    }
}
