package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.annotation.Navigate;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.*;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.core.session.SqlSession;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.cast;

public class NavigateBuilder<T>
{
    private final Config config;
    private final Class<T> targetClass;
    private final List<T> sources;
    private final List<SqlPropertyContext> includes;

    public NavigateBuilder(Config config, Class<T> targetClass, List<T> sources, List<SqlPropertyContext> includes)
    {
        this.config = config;
        this.targetClass = targetClass;
        this.sources = sources;
        this.includes = includes;
    }

    //select * from t0 left join t0.target = t1.self where t0.target in (?,?,?)
    public void build() throws InvocationTargetException, IllegalAccessException
    {
        MetaData metaData = MetaDataCache.getMetaData(targetClass);
        for (SqlPropertyContext include : includes)
        {
            Map<Object, T> sourcesMap = new HashMap<>();
            PropertyMetaData propertyMetaData = include.getPropertyMetaData();
            Navigate navigate = propertyMetaData.getNavigate();
            PropertyMetaData self = metaData.getPropertyMetaData(navigate.self());
            Class<?> navigateTargetType = propertyMetaData.getNavigateTargetType();
            PropertyMetaData target = MetaDataCache.getMetaData(navigateTargetType).getPropertyMetaData(navigate.target());
            for (T source : sources)
            {
                Object selfKey = self.getGetter().invoke(source);
                sourcesMap.put(selfKey, source);
            }
            Set<Object> objects = sourcesMap.keySet();
            QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
//            querySqlBuilder.setDistinct(true);
//            querySqlBuilder.addJoin(navigateTargetType, JoinType.LEFT, new SqlRealTableContext(navigateTargetType), new SqlBinaryContext(SqlOperator.EQ, new SqlPropertyContext(target, 0), new SqlPropertyContext(self, 1)));
            querySqlBuilder.addWhere(new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(target, 0), new SqlValueContext(sourcesMap.keySet())));
            String sql = querySqlBuilder.getSql();
            System.out.println(sql);
            List<PropertyMetaData> mappingData = querySqlBuilder.getMappingData();
            SqlSession session = config.getSqlSessionFactory().getSession();
            Map<Object, List<Object>> objectListMap = session.executeQuery(
                    r -> ObjectBuilder.start(r, cast(navigateTargetType), mappingData, false).createMapList(target),
                    sql,
                    Arrays.asList(sourcesMap.keySet().toArray())
            );
            for (Map.Entry<Object, List<Object>> objectListEntry : objectListMap.entrySet())
            {
                Object key = objectListEntry.getKey();
                List<Object> value = objectListEntry.getValue();
                T t = sourcesMap.get(key);
                propertyMetaData.getSetter().invoke(t, value);
            }
        }
    }
}
