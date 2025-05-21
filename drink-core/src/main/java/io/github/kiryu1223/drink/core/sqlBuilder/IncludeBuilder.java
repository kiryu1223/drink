package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.base.expression.ISqlCollectedValueExpression;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.NavigateData;
import io.github.kiryu1223.drink.base.session.SqlSession;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.beancreator.AbsBeanCreator;
import io.github.kiryu1223.drink.base.toBean.beancreator.BeanCreatorFactory;
import io.github.kiryu1223.drink.base.toBean.beancreator.IGetterCaller;
import io.github.kiryu1223.drink.base.toBean.beancreator.ISetterCaller;
import io.github.kiryu1223.drink.base.toBean.build.ObjectBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.kiryu1223.drink.base.annotation.RelationType.OneToOne;
import static io.github.kiryu1223.drink.base.util.DrinkUtil.cast;

public class IncludeBuilder {

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

    public void include(SqlSession session, Collection<?> source) throws InvocationTargetException, IllegalAccessException {
        NavigateData navigateData = includeField.getNavigateData();
        BeanCreatorFactory beanCreatorFactory = config.getBeanCreatorFactory();
        AbsBeanCreator<?> sourceBeanCreator = beanCreatorFactory.get(includeField.getParentType(), config);
        IGetterCaller<?, ?> sourceGetter = sourceBeanCreator.getBeanGetter(navigateData.getSelfFieldName());

        Collection<?> collection = collectedValue.getCollection();
        collection.clear();
        for (Object o : source) {
            collection.add(cast(sourceGetter.apply(cast(o))));
        }
        List<SqlValue> sqlValues = new ArrayList<>(source.size());
        String sql = subQuery.getSqlAndValue(config, sqlValues);

        RelationType relationType = navigateData.getRelationType();
        Class<?> targetType = navigateData.getNavigateTargetType();
        AbsBeanCreator<?> targetBeanCreator = beanCreatorFactory.get(targetType, config);
        IGetterCaller<?, ?> targetGetter = targetBeanCreator.getBeanGetter(navigateData.getTargetFieldName());
        ISetterCaller<?> includeSetter = sourceBeanCreator.getBeanSetter(includeField.getFieldName());

        switch (relationType) {
            case OneToOne:
            case ManyToOne:
                oneOneOrManyOne(
                        session,
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
                        session,
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
                        session,
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

    private void manyMany(SqlSession session, Collection<?> source, Class<?> targetType, NavigateData navigateData, String sql, List<SqlValue> sqlValues, IGetterCaller<?, ?> sourceGetter, ISetterCaller<?> includeSetter) throws InvocationTargetException, IllegalAccessException {
        Map<Object, ? extends List<?>> includeResultMap = session.executeQuery(rs -> ObjectBuilder.start(
                                rs,
                                targetType,
                                subQuery.getMappingData(config),
                                false,
                                config
                        )
                        .createMapListByAnotherKey(navigateData.getSelfMappingFieldMetaData(config), mappingKeyName),
                sql,
                sqlValues
        );

        Class<? extends Collection<?>> warpType = navigateData.getCollectionWrapperType();
        boolean isList = List.class.isAssignableFrom(warpType);
        boolean isSet = Set.class.isAssignableFrom(warpType);

        // 遍历源对象集合
        for (Object o : source) {
            // 获取源对象中关联字段的值作为key
            Object key = sourceGetter.apply(cast(o));
            // 获取关联对象集合
            List<?> valueList = includeResultMap.get(key);
            if (valueList != null) {
                if (isList) {
                    includeSetter.call(cast(o), valueList);
                }
                if (isSet) {
                    includeSetter.call(cast(o), new HashSet<>(valueList));
                }
            }
        }

        if (!includes.isEmpty()) {
            List<?> flatList = includeResultMap.values().stream().flatMap(o -> o.stream()).collect(Collectors.toList());
            recursion(session,flatList);
        }
    }

    private void oneMany(SqlSession session, Collection<?> source, Class<?> targetType, String sql, List<SqlValue> sqlValues, IGetterCaller<?, ?> targetGetter, NavigateData navigateData, IGetterCaller<?, ?> sourceGetter, ISetterCaller<?> includeSetter) throws InvocationTargetException, IllegalAccessException {
        List<?> includeResult = session.executeQuery(rs -> ObjectBuilder.start(
                                rs,
                                targetType,
                                subQuery.getMappingData(config),
                                false,
                                config
                        )
                        .createList(),
                sql,
                sqlValues
        );

        // <b.targetId,List<b>>
        Map<Object, List<Object>> map = new HashMap<>();
        for (Object o : includeResult) {
            Object key = targetGetter.apply(cast(o));
            List<Object> valueList = map.computeIfAbsent(key, k -> new ArrayList<>());
            valueList.add(o);
        }

        Class<? extends Collection<?>> warpType = navigateData.getCollectionWrapperType();
        boolean isList = List.class.isAssignableFrom(warpType);
        boolean isSet = Set.class.isAssignableFrom(warpType);
        // 遍历源对象集合
        for (Object o : source) {
            // 获取源对象中关联字段的值作为key
            Object key = sourceGetter.apply(cast(o));
            // 获取关联对象集合
            List<Object> valueList = map.get(key);
            if (valueList != null) {
                if (isList) {
                    includeSetter.call(cast(o), valueList);
                }
                if (isSet) {
                    includeSetter.call(cast(o), new HashSet<>(valueList));
                }
            }
        }

        if (!includes.isEmpty()) {
            List<Object> flatList = map.values().stream().flatMap(o -> o.stream()).collect(Collectors.toList());
            recursion(session,flatList);
        }
    }

    private void oneOneOrManyOne(SqlSession session, Collection<?> source, Class<?> targetType, String sql, List<SqlValue> sqlValues, IGetterCaller<?, ?> targetGetter, IGetterCaller<?, ?> sourceGetter, ISetterCaller<?> includeSetter, RelationType relationType) throws InvocationTargetException, IllegalAccessException {
        List<?> includeResult = session.executeQuery(rs -> ObjectBuilder.start(
                                rs,
                                targetType,
                                subQuery.getMappingData(config),
                                false,
                                config
                        )
                        .createList(),
                sql,
                sqlValues
        );

        // <b.targetId,b>
        Map<Object, Object> map = new HashMap<>();
        for (Object o : includeResult) {
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
            recursion(session, map.values());
        }
    }

    private void recursion(SqlSession session, Collection<?> source) throws InvocationTargetException, IllegalAccessException {
        for (IncludeBuilder include : includes) {
            include.include(session, source);
        }
    }
}
