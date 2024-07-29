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
    private final List<IncludeSet> includes;

    public IncludeBuilder(Config config, Class<T> targetClass, Collection<T> sources, List<IncludeSet> includes)
    {
        this.config = config;
        this.targetClass = targetClass;
        this.sources = sources;
        this.includes = includes;
    }

    public void include() throws InvocationTargetException, IllegalAccessException
    {
        MetaData targetClassMetaData = MetaDataCache.getMetaData(targetClass);
        Map<Object, T> sourcesMap = null;
        Map<Object, List<T>> sourcesMapList = null;
        for (IncludeSet include : includes)
        {
            NavigateData navigateData = include.getPropertyContext().getPropertyMetaData().getNavigateData();
            Class<?> navigateTargetType = navigateData.getNavigateTargetType();
            PropertyMetaData selfPropertyMetaData = targetClassMetaData.getPropertyMetaData(navigateData.getSelfPropertyName());
            PropertyMetaData targetPropertyMetaData = MetaDataCache.getMetaData(navigateTargetType).getPropertyMetaData(navigateData.getTargetPropertyName());
            PropertyMetaData includePropertyMetaData = include.getPropertyContext().getPropertyMetaData();

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
                if (sourcesMap == null)
                {
                    sourcesMap = getMap(selfPropertyMetaData);
                }
                // 构建sub查询
                QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
                SqlBinaryContext condition = new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlParensContext(new SqlValueContext(sourcesMap.keySet())));
                List<Object> values = new ArrayList<>(sourcesMap.keySet());
                // 如果有额外条件就加入
                if (include.hasCond())
                {
                    SqlContext cond = include.getCond();
                    List<Object> temp = new ArrayList<>();
                    cond.getSqlAndValue(config, temp);
                    values.addAll(temp);
                    SqlBinaryContext exCondition = new SqlBinaryContext(SqlOperator.AND, condition, new SqlParensContext(cond));
                    querySqlBuilder.addWhere(exCondition);
                }
                else
                {
                    querySqlBuilder.addWhere(condition);
                }
                String sql = querySqlBuilder.getSql();
                System.out.println(sql);
                List<PropertyMetaData> mappingData = querySqlBuilder.getMappingData(null);
                // 获取从表的map
                Map<Object, Object> objectMap = session.executeQuery(
                        r -> ObjectBuilder.start(r, cast(navigateTargetType), mappingData, false).createMap(targetPropertyMetaData.getColumn()),
                        sql,
                        values
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
                if (sourcesMap == null)
                {
                    sourcesMap = getMap(selfPropertyMetaData);
                }
                // 构建sub查询
                QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
                SqlBinaryContext condition = new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlParensContext(new SqlValueContext(sourcesMap.keySet())));
                List<Object> values = new ArrayList<>(sourcesMap.keySet());
                // 如果有额外条件就加入
                if (include.hasCond())
                {
                    SqlContext cond = include.getCond();
                    List<Object> temp = new ArrayList<>();
                    cond.getSqlAndValue(config, temp);
                    values.addAll(temp);
                    SqlBinaryContext exCondition = new SqlBinaryContext(SqlOperator.AND, condition, new SqlParensContext(cond));
                    querySqlBuilder.addWhere(exCondition);
                }
                else
                {
                    querySqlBuilder.addWhere(condition);
                }
                String sql = querySqlBuilder.getSql();
                System.out.println(sql);
                List<PropertyMetaData> mappingData = querySqlBuilder.getMappingData(null);

                // 查询从表数据，按key进行list归类的map构建
                Map<Object, List<Object>> objectListMap = session.executeQuery(
                        r -> ObjectBuilder.start(r, cast(navigateTargetType), mappingData, false).createMapList(targetPropertyMetaData.getColumn()),
                        sql,
                        values
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
                if (sourcesMapList == null)
                {
                    sourcesMapList = getMapList(selfPropertyMetaData);
                }
                // 构建sub查询
                QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
                SqlBinaryContext condition = new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlParensContext(new SqlValueContext(sourcesMapList.keySet())));
                List<Object> values = new ArrayList<>(sourcesMapList.keySet());
                // 如果有额外条件就加入
                if (include.hasCond())
                {
                    SqlContext cond = include.getCond();
                    List<Object> temp = new ArrayList<>();
                    cond.getSqlAndValue(config, temp);
                    values.addAll(temp);
                    SqlBinaryContext exCondition = new SqlBinaryContext(SqlOperator.AND, condition, new SqlParensContext(cond));
                    querySqlBuilder.addWhere(exCondition);
                }
                else
                {
                    querySqlBuilder.addWhere(condition);
                }
                String sql = querySqlBuilder.getSql();
                System.out.println(sql);
                List<PropertyMetaData> mappingData = querySqlBuilder.getMappingData(null);
                // 获取目标表的map
                Map<Object, Object> objectMap = session.executeQuery(
                        r -> ObjectBuilder.start(r, cast(navigateTargetType), mappingData, false).createMap(targetPropertyMetaData.getColumn()),
                        sql,
                        values
                );
                // 多对一赋值
                for (Map.Entry<Object, Object> objectEntry : objectMap.entrySet())
                {
                    Object key = objectEntry.getKey();
                    Object value = objectEntry.getValue();
                    for (T t : sourcesMapList.get(key))
                    {
                        includePropertyMetaData.getSetter().invoke(t, value);
                    }
                }
            }

            // 多对多情况
            // 多个自身表的字段(self)与多个目标表字段(target)对应
            else if (navigateData.getRelationType() == RelationType.ManyToMany)
            {

                if (sourcesMapList == null)
                {
                    sourcesMapList = getMapList(selfPropertyMetaData);
                }
                Class<? extends IMappingTable> mappingTableType = navigateData.getMappingTableType();
                MetaData mappingTableMetadata = MetaDataCache.getMetaData(mappingTableType);
                String selfMappingPropertyName = navigateData.getSelfMappingPropertyName();
                PropertyMetaData selfMappingPropertyMetaData = mappingTableMetadata.getPropertyMetaData(selfMappingPropertyName);
                String targetMappingPropertyName = navigateData.getTargetMappingPropertyName();
                PropertyMetaData targetMappingPropertyMetaData = mappingTableMetadata.getPropertyMetaData(targetMappingPropertyName);
                // 构建一个目标表查询，leftJoin中间表，join条件为目标表字段与中间表的目标映射字段相等
                // FROM navigateTargetType as t LEFT JOIN mappingTableType as ON t.target = m.targetMapping
                QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
                List<PropertyMetaData> mappingData = querySqlBuilder.getMappingData(null);
                querySqlBuilder.addJoin(JoinType.LEFT, new SqlRealTableContext(mappingTableType), new SqlBinaryContext(SqlOperator.EQ, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlPropertyContext(targetMappingPropertyMetaData, 1)));
                SqlBinaryContext condition = new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(selfMappingPropertyMetaData, 1), new SqlParensContext(new SqlValueContext(sourcesMapList.keySet())));
                List<Object> values = new ArrayList<>(sourcesMapList.keySet());
                // 如果有额外条件就加入
                if (include.hasCond())
                {
                    SqlContext cond = include.getCond();
                    List<Object> temp = new ArrayList<>();
                    cond.getSqlAndValue(config, temp);
                    values.addAll(temp);
                    SqlBinaryContext exCondition = new SqlBinaryContext(SqlOperator.AND, condition, new SqlParensContext(cond));
                    querySqlBuilder.addWhere(exCondition);
                }
                else
                {
                    querySqlBuilder.addWhere(condition);
                }
                SqlSelectorContext select = (SqlSelectorContext) querySqlBuilder.getSelect();
                select.getSqlContexts().add(new SqlPropertyContext(selfMappingPropertyMetaData, 1));
                mappingData.add(selfMappingPropertyMetaData);
                String sql = querySqlBuilder.getSql();
                System.out.println(sql);

                Map<Object, List<Object>> targetMap = session.executeQuery(
                        r -> ObjectBuilder.start(r, cast(navigateTargetType), mappingData, false).createMapListByAnotherKey(selfMappingPropertyMetaData.getColumn()),
                        sql,
                        values
                );
                for (Map.Entry<Object, List<T>> objectEntry : sourcesMapList.entrySet())
                {
                    Object key = objectEntry.getKey();
                    List<T> value = objectEntry.getValue();
                    List<Object> targetValues = targetMap.get(key);
                    for (T t : value)
                    {
                        includePropertyMetaData.getSetter().invoke(t, targetValues);
                    }
                }
            }
        }
    }

    private <K> Map<K, T> getMap(PropertyMetaData propertyMetaData) throws InvocationTargetException, IllegalAccessException
    {
        Map<K, T> sourcesMap = new HashMap<>();
        for (T source : sources)
        {
            K selfKey = (K) propertyMetaData.getGetter().invoke(source);
            sourcesMap.put(selfKey, source);
        }
        return sourcesMap;
    }

    private <K> Map<K, List<T>> getMapList(PropertyMetaData propertyMetaData) throws InvocationTargetException, IllegalAccessException
    {
        Map<K, List<T>> sourcesMapList = new HashMap<>();
        for (T source : sources)
        {
            K selfKey = (K) propertyMetaData.getGetter().invoke(source);
            if (!sourcesMapList.containsKey(selfKey))
            {
                List<T> list = new ArrayList<>();
                list.add(source);
                sourcesMapList.put(selfKey, list);
            }
            else
            {
                sourcesMapList.get(selfKey).add(source);
            }
        }
        return sourcesMapList;
    }
}
