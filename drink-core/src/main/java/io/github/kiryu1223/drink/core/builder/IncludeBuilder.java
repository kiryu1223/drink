package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.annotation.RelationType;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.*;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.NavigateData;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.core.session.SqlSession;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.cast;

public class IncludeBuilder<T>
{
    private final Config config;
    private final Class<T> targetClass;
    private final Collection<T> sources;
    private final List<SqlPropertyContext> includes;

    public IncludeBuilder(Config config, Class<T> targetClass, Collection<T> sources, List<SqlPropertyContext> includes)
    {
        this.config = config;
        this.targetClass = targetClass;
        this.sources = sources;
        this.includes = includes;
    }

    // select t0.* from t0 WHERE t0.target in (? * n)
    public void build() throws InvocationTargetException, IllegalAccessException
    {
        MetaData metaData = MetaDataCache.getMetaData(targetClass);
        for (SqlPropertyContext include : includes)
        {
            Map<Object, T> sourcesMap = new HashMap<>();
            PropertyMetaData propertyMetaData = include.getPropertyMetaData();
            NavigateData navigateData = propertyMetaData.getNavigateData();
            PropertyMetaData self = metaData.getPropertyMetaData(navigateData.getSelfPropertyName());
            Class<?> navigateTargetType = navigateData.getNavigateTargetType();
            PropertyMetaData target = MetaDataCache.getMetaData(navigateTargetType).getPropertyMetaData(navigateData.getTargetPropertyName());
            for (T source : sources)
            {
                Object selfKey = self.getGetter().invoke(source);
                sourcesMap.put(selfKey, source);
            }
            Set<Object> objects = sourcesMap.keySet();
            QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
            querySqlBuilder.addWhere(new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(target, 0), new SqlValueContext(sourcesMap.keySet())));
            String sql = querySqlBuilder.getSql();
            System.out.println(sql);
            List<PropertyMetaData> mappingData = querySqlBuilder.getMappingData(null);
            SqlSession session = config.getSqlSessionFactory().getSession();

            // 一对多
            if (navigateData.getRelationType() == RelationType.OneToMany)
            {
                Map<Object, List<Object>> objectListMap = session.executeQuery(
                        r -> ObjectBuilder.start(r, cast(navigateTargetType), mappingData, false).createMapList(target.getColumn()),
                        sql,
                        sourcesMap.keySet()
                );
                for (Map.Entry<Object, List<Object>> objectListEntry : objectListMap.entrySet())
                {
                    Object key = objectListEntry.getKey();
                    List<Object> value = objectListEntry.getValue();
                    T t = sourcesMap.get(key);
                    propertyMetaData.getSetter().invoke(t, value);
                }
            }
            // 一对一
            else if (navigateData.getRelationType() == RelationType.OneToOne)
            {
                Map<Object, Object> objectList = session.executeQuery(
                        r -> ObjectBuilder.start(r, cast(navigateTargetType), mappingData, false).createMap(target.getColumn()),
                        sql,
                        sourcesMap.keySet()
                );
                for (Map.Entry<Object, Object> objectListEntry : objectList.entrySet())
                {
                    Object key = objectListEntry.getKey();
                    Object value = objectListEntry.getValue();
                    T t = sourcesMap.get(key);
                    propertyMetaData.getSetter().invoke(t, value);
                }
            }

        }
    }
}
