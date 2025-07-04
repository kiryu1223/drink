package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.beancreator.AbsBeanCreator;
import io.github.kiryu1223.drink.base.toBean.beancreator.ISetterCaller;
import io.github.kiryu1223.drink.base.toBean.build.ExtensionField;
import io.github.kiryu1223.drink.base.toBean.build.ExtensionObject;
import io.github.kiryu1223.drink.base.toBean.build.JdbcResult;
import io.github.kiryu1223.drink.base.toBean.build.ObjectBuilder;
import io.github.kiryu1223.drink.base.toBean.executor.CreateBeanExecutor;
import io.github.kiryu1223.drink.base.toBean.executor.JdbcExecutor;
import io.github.kiryu1223.drink.base.toBean.executor.JdbcQueryResultSet;
import io.github.kiryu1223.drink.base.util.DrinkUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.cast;

public class SubQueryBuilder
{
    private final IConfig config;
    private final SqlExpressionFactory factory;
    // 需要被注入的字段
    private final FieldMetaData fieldMetaData;
    // 子查询
    private ISqlQueryableExpression queryableExpression;

    public SubQueryBuilder(IConfig config, FieldMetaData fieldMetaData, ISqlQueryableExpression queryableExpression)
    {
        this.config = config;
        this.fieldMetaData = fieldMetaData;
        this.queryableExpression = queryableExpression;
        factory = config.getSqlExpressionFactory();
    }

    static class Values
    {
        private final int level;
        private final Map<String, Value> map = new HashMap<>();
        private final int count;
        private int index = -1;

        Values(int level,int count)
        {
            this.level = level;
            this.count=count;
        }

        private boolean next()
        {
            return ++index < count;
        }

        private void reset()
        {
            index = -1;
        }

        private Object getValue(String valueName)
        {
            return map.get(valueName).getValue(index);
        }

        private void addValue(String valueName, Value value)
        {
            map.put(valueName, value);
        }
    }

    static class Value
    {
        private final FieldMetaData fieldMetaData;
        private final List<Object> values = new ArrayList<>();

        Value(FieldMetaData fieldMetaData)
        {
            this.fieldMetaData = fieldMetaData;
        }

        private Object getValue(int index)
        {
            return values.get(index);
        }

        private void addValue(Object value)
        {
            values.add(value);
        }

        @Override
        public String toString()
        {
            return "Value{" +
                    "fieldMetaData=" + fieldMetaData +
                    ", values=" + values +
                    '}';
        }
    }

    public void subQuery(
            // 原始集合
            List<JdbcResult<?>> jdbcResultList
            // 上下文参数
            // select *,? as `value:id`,? as `value:name`
    ) throws InvocationTargetException, IllegalAccessException
    {

        Map<Integer, Values> valuesMap = new HashMap<>();
        queryableExpression.accept(new SqlTreeVisitor()
        {
            @Override
            public void visit(ISqlSingleValueExpression expr)
            {
                if (expr instanceof SubQueryValue)
                {
                    SubQueryValue subQueryValue = (SubQueryValue) expr;
                    int level = subQueryValue.getLevel();
                    // 层级匹配
                    JdbcResult<?> jdbcResult = jdbcResultList.get(level);
                    String valueName = subQueryValue.getValueName();
                    Values values = valuesMap.computeIfAbsent(level, k -> new Values(k,jdbcResult.getExtensionValueResult().size()));
                    Value value = new Value(subQueryValue.getFieldMetaData());
                    for (ExtensionObject extensionObject : jdbcResult.getExtensionValueResult())
                    {
                        ExtensionField extensionField = extensionObject.getExtensionFieldMap().get(valueName);
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
        for (Values values : sorted)
        {
            values.map.forEach((valueName, value) ->
            {
                SubQueryValue sq = new SubQueryValue(value.fieldMetaData, values.level);
                // 拼到select里
                select.addColumn(factory.as(sq, sq.getValueName()));
                // 添加到exValues的额外key里
                exValues.addExKey(sq.getValueName(), value.fieldMetaData);
            });
        }

        List<ISqlQueryableExpression> list = new ArrayList<>();
        SqlTreeVisitor visitor = new SqlTreeVisitor()
        {
            @Override
            public void visit(ISqlSingleValueExpression expr)
            {
                super.visit(expr);
                if (expr instanceof SubQueryValue)
                {
                    SubQueryValue subQueryValue = (SubQueryValue) expr;
                    String valueName = subQueryValue.getValueName();
                    int level = subQueryValue.getLevel();
                    Object value = sorted.stream().filter(v -> v.level == level).findAny().get().getValue(valueName);
                    subQueryValue.setValue(value);
                }
            }
        };
        while (next(sorted))
        {
            ISqlQueryableExpression copy = queryableExpression.copy(config);
            copy.accept(visitor);
            list.add(copy);
        }

        ISqlUnionQueryableExpression iSqlUnionQueryableExpression = factory.unionQueryable(list, false);
        List<SqlValue> sqlValues = new ArrayList<>();
        AsNameManager.start();
        String sql = iSqlUnionQueryableExpression.getSqlAndValue(config, sqlValues);
        AsNameManager.clear();

        JdbcQueryResultSet jdbcQueryResultSet = JdbcExecutor.executeQuery(config, sql, sqlValues);
        JdbcResult<?> jdbcResult = CreateBeanExecutor.classList(config, jdbcQueryResultSet, iSqlUnionQueryableExpression.getMainTableClass());

        AbsBeanCreator<?> absBeanCreator = config.getBeanCreatorFactory().get(fieldMetaData.getParentType());
        ISetterCaller<?> beanSetter = absBeanCreator.getBeanSetter(fieldMetaData.getFieldName());

        JdbcResult<?> currentResult = jdbcResultList.get(jdbcResultList.size() - 1);
        for (ExtensionObject extensionObject : currentResult.getExtensionValueResult())
        {
            Map<String, ExtensionField> valueFieldMap = extensionObject.getExtensionFieldMap();
            for (ExtensionObject eob : jdbcResult.getExtensionKeyResult())
            {
                Map<String, ExtensionField> keyFieldMap = eob.getExtensionFieldMap();
                if (compareCommonValues(keyFieldMap, valueFieldMap))
                {
                    setValue(extensionObject.getObjects(), beanSetter, eob.getObjects());
                }
            }
        }

        // 递归向下子查询
        List<SubQueryBuilder> subQueryBuilders = queryableExpression.getSelect().getSubQueryBuilders();
        if(!subQueryBuilders.isEmpty())
        {
            jdbcResultList.add(jdbcResult);
            for (SubQueryBuilder subQueryBuilder : subQueryBuilders)
            {
                subQueryBuilder.subQuery(jdbcResultList);
            }
            jdbcResultList.remove(jdbcResultList.size() - 1);
        }
    }

    public static boolean compareCommonValues(Map<String, ExtensionField> smallMap, Map<String, ExtensionField> bigMap)
    {
        return smallMap.entrySet().stream()
                .filter(entry -> bigMap.containsKey(entry.getKey())) // 只过滤大Map存在的Key
                .allMatch(entry -> Objects.equals(entry.getValue().getValue(), bigMap.get(entry.getKey()).getValue()));
    }

    private void setValue(List<Object> objects, ISetterCaller<?> beanSetter, List<Object> values) throws InvocationTargetException, IllegalAccessException
    {
        Class<?> type = fieldMetaData.getType();
        boolean isColl = DrinkUtil.isCollection(type);
        // 如果字段是集合
        if (isColl)
        {
            if (DrinkUtil.isList(type))
            {
                for (Object object : objects)
                {
                    beanSetter.call(cast(object), values);
                }
            }
            else if (DrinkUtil.isSet(type))
            {
                for (Object object : objects)
                {
                    beanSetter.call(cast(object), new HashSet<>(values));
                }
            }
            else
            {
                throw new DrinkException(type.getName());
            }
        }
        // 如果不是集合
        else
        {
            Object o = values.get(0);
            if (type.isAssignableFrom(o.getClass()))
            {
                for (Object object : objects)
                {
                    beanSetter.call(cast(object), o);
                }
            }
            else
            {
                throw new DrinkException(type + " " + o.getClass());
            }
        }
    }

    private boolean next(List<Values> valuesList)
    {
        return next(valuesList, valuesList.size() - 1);
    }

    private boolean next(List<Values> valuesList, int index)
    {
        if (index < 0 || index >= valuesList.size())
        {
            return false;
        }
        Values values = valuesList.get(index);
        if (values.next())
        {
            return true;
        }
        else
        {
            values.reset();
            return next(valuesList, index - 1);
        }
    }

    public ISqlQueryableExpression getQueryableExpression()
    {
        return queryableExpression;
    }

    public void setQueryableExpression(ISqlQueryableExpression queryableExpression)
    {
        this.queryableExpression = queryableExpression;
    }
}
