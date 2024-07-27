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
import io.github.kiryu1223.drink.ext.IMappingTable;

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

    public void include() throws InvocationTargetException, IllegalAccessException
    {
        MetaData targetClassMetaData = MetaDataCache.getMetaData(targetClass);
        for (SqlPropertyContext include : includes)
        {
            NavigateData navigateData = include.getPropertyMetaData().getNavigateData();
            Class<?> navigateTargetType = navigateData.getNavigateTargetType();
            PropertyMetaData selfPropertyMetaData = targetClassMetaData.getPropertyMetaData(navigateData.getSelfPropertyName());
            PropertyMetaData targetPropertyMetaData = MetaDataCache.getMetaData(navigateTargetType).getPropertyMetaData(navigateData.getTargetPropertyName());
            PropertyMetaData includePropertyMetaData = include.getPropertyMetaData();

            SqlSession session = config.getSqlSessionFactory().getSession();
            // 一对一情况
            // 主表的字段(self)与从表的字段(target)一对一对应
            // 因为是一对一对应的，所以主表查完自身的数据之后，可以将self为key返回结果为value映射为一个map
            // 然后对从表进行查询，条件为从表的target IN map的keySet（从表的target是否为keySet中的某个）
            // 从表的返回结果以target为key返回结果为value映射为map
            // 遍历从表返回的map，每轮从主表map以key获取一个主表对象，把value反射进主表对象

            if (navigateData.getRelationType() == RelationType.OneToOne)
            {
                // 遍历结果提取key映射到map
                Map<Object, T> sourcesMap = new HashMap<>();
                for (T source : sources)
                {
                    Object selfKey = selfPropertyMetaData.getGetter().invoke(source);
                    sourcesMap.put(selfKey, source);
                }
                // 构建sub查询
                QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
                querySqlBuilder.addWhere(new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlParensContext(new SqlValueContext(sourcesMap.keySet()))));
                String sql = querySqlBuilder.getSql();
                System.out.println(sql);
                List<PropertyMetaData> mappingData = querySqlBuilder.getMappingData(null);
                // 获取从表的map
                Map<Object, Object> objectMap = session.executeQuery(
                        r -> ObjectBuilder.start(r, cast(navigateTargetType), mappingData, false).createMap(targetPropertyMetaData.getColumn()),
                        sql,
                        sourcesMap.keySet()
                );
                // 一对一赋值
                for (Map.Entry<Object, Object> objectEntry : objectMap.entrySet())
                {
                    Object key = objectEntry.getKey();
                    Object value = objectEntry.getValue();
                    T t = sourcesMap.get(key);
                    includePropertyMetaData.getSetter().invoke(t, value);
                }
            }

            // 一对多情况
            // 主表的字段(self)与多个从表的字段(target)对应
            else if (navigateData.getRelationType() == RelationType.OneToMany)
            {
                // 遍历结果提取key映射到map
                Map<Object, T> sourcesMap = new HashMap<>();
                for (T source : sources)
                {
                    Object selfKey = selfPropertyMetaData.getGetter().invoke(source);
                    sourcesMap.put(selfKey, source);
                }
                // 构建sub查询
                QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
                querySqlBuilder.addWhere(new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlParensContext(new SqlValueContext(sourcesMap.keySet()))));
                String sql = querySqlBuilder.getSql();
                System.out.println(sql);
                List<PropertyMetaData> mappingData = querySqlBuilder.getMappingData(null);
                // 查询从表数据，按key进行list归类的map构建
                Map<Object, List<Object>> objectListMap = session.executeQuery(
                        r -> ObjectBuilder.start(r, cast(navigateTargetType), mappingData, false).createMapList(targetPropertyMetaData.getColumn()),
                        sql,
                        sourcesMap.keySet()
                );
                // 一对多赋值
                for (Map.Entry<Object, List<Object>> objectListEntry : objectListMap.entrySet())
                {
                    Object key = objectListEntry.getKey();
                    List<Object> value = objectListEntry.getValue();
                    T t = sourcesMap.get(key);
                    includePropertyMetaData.getSetter().invoke(t, value);
                }
            }

            // 多对一情况
            // 目标表的字段(target)与多个自身表的字段(self)对应
            else if (navigateData.getRelationType() == RelationType.ManyToOne)
            {
                // 这个时候我们不能使用Map<K, T>，因为T不再是单一存在的
                // 我们使用Map<K, List<T>>
                Map<Object, List<T>> sourcesMap = new HashMap<>();
                for (T source : sources)
                {
                    Object selfKey = selfPropertyMetaData.getGetter().invoke(source);
                    if (!sourcesMap.containsKey(selfKey))
                    {
                        List<T> list = new ArrayList<>();
                        list.add(source);
                        sourcesMap.put(selfKey, list);
                    }
                    else
                    {
                        sourcesMap.get(selfKey).add(source);
                    }
                }
                // 构建sub查询
                QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
                querySqlBuilder.addWhere(new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlParensContext(new SqlValueContext(sourcesMap.keySet()))));
                String sql = querySqlBuilder.getSql();
                System.out.println(sql);
                List<PropertyMetaData> mappingData = querySqlBuilder.getMappingData(null);
                // 获取目标表的map
                Map<Object, Object> objectMap = session.executeQuery(
                        r -> ObjectBuilder.start(r, cast(navigateTargetType), mappingData, false).createMap(targetPropertyMetaData.getColumn()),
                        sql,
                        sourcesMap.keySet()
                );
                // 多对一赋值
                for (Map.Entry<Object, Object> objectEntry : objectMap.entrySet())
                {
                    Object key = objectEntry.getKey();
                    Object value = objectEntry.getValue();
                    for (T t : sourcesMap.get(key))
                    {
                        includePropertyMetaData.getSetter().invoke(t, value);
                    }
                }
            }

            // 多对多情况
            // 多个自身表的字段(self)与多个目标表字段(target)对应
            else if (navigateData.getRelationType() == RelationType.ManyToMany)
            {
                // 这个时候我们不能使用Map<K, T>，因为T不再是单一存在的
                // 我们使用Map<K, List<T>>
                Map<Object, List<T>> sourcesMap = new HashMap<>();
                for (T source : sources)
                {
                    Object selfKey = selfPropertyMetaData.getGetter().invoke(source);
                    if (!sourcesMap.containsKey(selfKey))
                    {
                        List<T> list = new ArrayList<>();
                        list.add(source);
                        sourcesMap.put(selfKey, list);
                    }
                    else
                    {
                        sourcesMap.get(selfKey).add(source);
                    }
                }
                Class<? extends IMappingTable> mappingTableType = navigateData.getMappingTableType();
                MetaData mappingTableMetadata = MetaDataCache.getMetaData(mappingTableType);
                // 先查询中间表获取目标字段的值
                QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(mappingTableType));
                String selfMappingPropertyName = navigateData.getSelfMappingPropertyName();
                PropertyMetaData selfMappingPropertyMetaData = mappingTableMetadata.getPropertyMetaData(selfMappingPropertyName);
                querySqlBuilder.addWhere(new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(selfMappingPropertyMetaData, 0), new SqlParensContext(new SqlValueContext(sourcesMap.keySet()))));
                String sql = querySqlBuilder.getSql();
                System.out.println(sql);

                // 获取中间表的map
                List<PropertyMetaData> mappingData = querySqlBuilder.getMappingData(null);
                String targetMappingPropertyName = navigateData.getTargetMappingPropertyName();
                PropertyMetaData targetMappingPropertyMetaData = mappingTableMetadata.getPropertyMetaData(targetMappingPropertyName);
                Map<Object, ? extends IMappingTable> mappingMap = session.executeQuery(
                        r -> ObjectBuilder.start(r, mappingTableType, mappingData, false).createMap(targetMappingPropertyMetaData.getColumn()),
                        sql,
                        sourcesMap.keySet()
                );
                System.out.println(mappingMap);
                System.out.println(mappingMap.size());
                // 再查询目标表获取目标数据
                QuerySqlBuilder querySqlBuilder2 = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
                querySqlBuilder2.addWhere(new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlParensContext(new SqlValueContext(mappingMap.keySet()))));
                String sql2 = querySqlBuilder2.getSql();
                System.out.println(sql2);
                List<PropertyMetaData> mappingData2 = querySqlBuilder2.getMappingData(null);
                Map<Object, List<Object>> targetData = session.executeQuery(
                        r -> ObjectBuilder.start(r, cast(navigateTargetType), mappingData2, false).createMapList(targetPropertyMetaData.getColumn()),
                        sql2,
                        mappingMap.keySet()
                );

                Map<Object, List<Object>> midMap = new HashMap<>();
                for (Map.Entry<Object, ? extends IMappingTable> objectEntry : mappingMap.entrySet())
                {
                    Object mappingTargetKey = objectEntry.getKey();
                    List<Object> objects = targetData.get(mappingTargetKey);
                    IMappingTable mappingTable = objectEntry.getValue();
                    Object key = selfMappingPropertyMetaData.getGetter().invoke(mappingTable);
                    midMap.put(key, objects);
                }

                for (Map.Entry<Object, List<T>> objectEntry : sourcesMap.entrySet())
                {
                    Object key = objectEntry.getKey();
                    List<T> objects = objectEntry.getValue();
                    List<Object> values = midMap.get(key);
                    System.out.println(key);
                    System.out.println(values);
                    for (T object : objects)
                    {
                        includePropertyMetaData.getSetter().invoke(object, values);
                    }
                }
            }
        }
    }
}
