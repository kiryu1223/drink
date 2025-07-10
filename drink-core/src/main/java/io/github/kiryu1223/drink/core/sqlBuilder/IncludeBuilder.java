package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.base.expression.ISqlCollectedValueExpression;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.log.ISqlLogger;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.NavigateData;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.beancreator.AbsBeanCreator;
import io.github.kiryu1223.drink.base.toBean.beancreator.BeanCreatorFactory;
import io.github.kiryu1223.drink.base.toBean.beancreator.IGetterCaller;
import io.github.kiryu1223.drink.base.toBean.beancreator.ISetterCaller;
import io.github.kiryu1223.drink.base.toBean.build.JdbcResult;
import io.github.kiryu1223.drink.base.util.DrinkUtil;
import io.github.kiryu1223.drink.base.toBean.executor.CreateBeanExecutor;
import io.github.kiryu1223.drink.base.toBean.executor.JdbcExecutor;
import io.github.kiryu1223.drink.base.toBean.executor.JdbcQueryResultSet;
import io.github.kiryu1223.drink.core.util.ExpressionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.kiryu1223.drink.base.annotation.RelationType.OneToOne;
import static io.github.kiryu1223.drink.base.util.DrinkUtil.cast;

public class IncludeBuilder {

    private static final Logger log = LoggerFactory.getLogger(IncludeBuilder.class);
    private final IConfig config;
    private final ISqlQueryableExpression subQuery;
    private final ISqlCollectedValueExpression collectedValue;
    private final FieldMetaData includeField;
    private final String mappingKeyName;
    private final List<IncludeBuilder> includes = new ArrayList<>();

    public IncludeBuilder(IConfig config, ISqlQueryableExpression subQuery, ISqlCollectedValueExpression collectedValue, FieldMetaData fieldMetaData, String mappingKeyName) {
        this.config = config;
        this.subQuery = subQuery;
        this.collectedValue = collectedValue;
        this.includeField = fieldMetaData;
        this.mappingKeyName = mappingKeyName;
    }

    public List<IncludeBuilder> getIncludes() {
        return includes;
    }

    public void include(Collection<?> source) throws InvocationTargetException, IllegalAccessException {
        if (source.isEmpty()) return;

        NavigateData navigateData = includeField.getNavigateData();
        BeanCreatorFactory beanCreatorFactory = config.getBeanCreatorFactory();
        AbsBeanCreator<?> sourceBeanCreator = beanCreatorFactory.get(includeField.getParentType());
        IGetterCaller<?, ?> sourceGetter = sourceBeanCreator.getBeanGetter(navigateData.getSelfFieldName());

        RelationType relationType = navigateData.getRelationType();
        Class<?> targetType = navigateData.getNavigateTargetType();
        AbsBeanCreator<?> targetBeanCreator = beanCreatorFactory.get(targetType);
        IGetterCaller<?, ?> targetGetter = targetBeanCreator.getBeanGetter(navigateData.getTargetFieldName());
        ISetterCaller<?> includeSetter = sourceBeanCreator.getBeanSetter(includeField.getFieldName());

        Collection<?> collection = collectedValue.getCollection();
        collection.clear();
        for (Object o : source) {
            collection.add(cast(sourceGetter.apply(cast(o))));
        }
        QuerySqlBuilder builder = new QuerySqlBuilder(config, subQuery);
        List<SqlValue> sqlValues = new ArrayList<>(source.size());
        String sql = builder.getSqlAndValue(sqlValues);

        ISqlLogger logger = config.getSqlLogger();
        logger.printSql(sql);

        switch (relationType) {
            case OneToOne:
            case ManyToOne:
                oneOneOrManyOne(
                        source,
                        targetType,
                        sql,
                        sqlValues,
                        targetGetter,
                        sourceGetter,
                        includeSetter,
                        relationType
                );
                break;
            case OneToMany:
                oneMany(
                        source,
                        targetType,
                        sql,
                        sqlValues,
                        targetGetter,
                        navigateData,
                        sourceGetter,
                        includeSetter
                );
                break;
            case ManyToMany: {
                manyMany(
                        source,
                        targetType,
                        navigateData,
                        sql,
                        sqlValues,
                        sourceGetter,
                        includeSetter
                );
            }
            break;
        }
    }

    private void manyMany(Collection<?> source, Class<?> targetType, NavigateData navigateData, String sql, List<SqlValue> sqlValues, IGetterCaller<?, ?> sourceGetter, ISetterCaller<?> includeSetter) throws InvocationTargetException, IllegalAccessException {

        JdbcQueryResultSet jdbcQueryResultSet = JdbcExecutor.executeQuery(config, sql, sqlValues);
        Map<Object, ? extends List<?>> includeResultMap = CreateBeanExecutor.classListAnotherKeyMap(config, jdbcQueryResultSet, targetType, navigateData.getSelfMappingFieldMetaData(config),mappingKeyName);

        Class<? extends Collection<?>> warpType = navigateData.getCollectionWrapperType();
        boolean isDrinkList = ExpressionUtil.isDrinkList(warpType);
        boolean isList = DrinkUtil.isList(warpType);
        boolean isSet = DrinkUtil.isSet(warpType);
        // 遍历源对象集合
        for (Object o : source) {
            // 获取源对象中关联字段的值作为key
            Object key = sourceGetter.apply(cast(o));
            // 获取关联对象集合
            List<?> valueList = includeResultMap.get(key);
            if (valueList != null) {
                if (isDrinkList) {
                    includeSetter.call(cast(o), new io.github.kiryu1223.drink.core.util.List<>(valueList));
                }
                else if (isList) {
                    includeSetter.call(cast(o), valueList);
                }
                else if (isSet) {
                    includeSetter.call(cast(o), new HashSet<>(valueList));
                }
            }
        }

        if (!includes.isEmpty()) {
            List<?> flatList = includeResultMap.values().stream().flatMap(o -> o.stream()).collect(Collectors.toList());
            recursion(flatList);
        }
    }

    private void oneMany(Collection<?> source, Class<?> targetType, String sql, List<SqlValue> sqlValues, IGetterCaller<?, ?> targetGetter, NavigateData navigateData, IGetterCaller<?, ?> sourceGetter, ISetterCaller<?> includeSetter) throws InvocationTargetException, IllegalAccessException {
//        JdbcResult<?> includeResult = session.executeQuery(resultSet -> ObjectBuilder.start(
//                                resultSet,
//                                targetType,
//                                subQuery.getMappingData(config),
//                                false,
//                                config
//                        )
//                        .createList(),
//                sql,
//                sqlValues
//        );

        JdbcQueryResultSet jdbcQueryResultSet = JdbcExecutor.executeQuery(config, sql, sqlValues);
        JdbcResult<?> includeResult = CreateBeanExecutor.classList(config, jdbcQueryResultSet, targetType);

        // <b.targetId,List<b>>
        Map<Object, List<Object>> map = new HashMap<>();
        for (Object o : includeResult.getResult()) {
            Object key = targetGetter.apply(cast(o));
            List<Object> valueList = map.computeIfAbsent(key, k -> new ArrayList<>());
            valueList.add(o);
        }

        Class<? extends Collection<?>> warpType = navigateData.getCollectionWrapperType();
        boolean isDrinkList = ExpressionUtil.isDrinkList(warpType);
        boolean isList = DrinkUtil.isList(warpType);
        boolean isSet = DrinkUtil.isSet(warpType);
        // 遍历源对象集合
        for (Object o : source) {
            // 获取源对象中关联字段的值作为key
            Object key = sourceGetter.apply(cast(o));
            // 获取关联对象集合
            List<Object> valueList = map.get(key);
            if (valueList != null) {
                if (isDrinkList) {
                    includeSetter.call(cast(o), new io.github.kiryu1223.drink.core.util.List<>(valueList));
                }
                else if (isList) {
                    includeSetter.call(cast(o), valueList);
                }
                else if (isSet) {
                    includeSetter.call(cast(o), new HashSet<>(valueList));
                }
            }
        }

        if (!includes.isEmpty()) {
            List<Object> flatList = map.values().stream().flatMap(o -> o.stream()).collect(Collectors.toList());
            recursion(flatList);
        }
    }

    private void oneOneOrManyOne(Collection<?> source, Class<?> targetType, String sql, List<SqlValue> sqlValues, IGetterCaller<?, ?> targetGetter, IGetterCaller<?, ?> sourceGetter, ISetterCaller<?> includeSetter, RelationType relationType) throws InvocationTargetException, IllegalAccessException {
//        JdbcResult<?> includeResult = session.executeQuery(resultSet -> ObjectBuilder.start(
//                                resultSet,
//                                targetType,
//                                subQuery.getMappingData(config),
//                                false,
//                                config
//                        )
//                        .createList(),
//                sql,
//                sqlValues
//        );

        JdbcQueryResultSet jdbcQueryResultSet = JdbcExecutor.executeQuery(config, sql, sqlValues);
        JdbcResult<?> includeResult = CreateBeanExecutor.classList(config, jdbcQueryResultSet, targetType);

        // <b.targetId,b>
        Map<Object, Object> map = new HashMap<>();
        for (Object o : includeResult.getResult()) {
            map.put(targetGetter.apply(cast(o)), o);
        }

        // 遍历源对象集合
        for (Object o : source) {
            // 获取源对象中关联字段的值作为key
            Object key = sourceGetter.apply(cast(o));
            // 获取关联对象
            Object value = map.get(key);
            if (value != null) {
                includeSetter.call(cast(o), value);
                // 如果是oneToOne，则从map中移除
                if (relationType == OneToOne) {
                    map.remove(key);
                }
            }
        }

        if (!includes.isEmpty()) {
            recursion(map.values());
        }
    }

    private void recursion(Collection<?> source) throws InvocationTargetException, IllegalAccessException {
        for (IncludeBuilder include : includes) {
            include.include(source);
        }
    }
}
