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
import java.util.stream.Collectors;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.cast;

public class IncludeBuilder<T>
{
    private final Config config;
    private final Class<T> targetClass;
    private final Collection<T> sources;
    private final List<IncludeSet> includes;
    private final QuerySqlBuilder mainSqlBuilder;

    public IncludeBuilder(Config config, Class<T> targetClass, Collection<T> sources, List<IncludeSet> includes, QuerySqlBuilder mainSqlBuilder)
    {
        this.config = config;
        this.targetClass = targetClass;
        this.sources = sources;
        this.includes = includes;
        this.mainSqlBuilder = mainSqlBuilder;
    }

    public void include() throws InvocationTargetException, IllegalAccessException
    {
        MetaData targetClassMetaData = MetaDataCache.getMetaData(targetClass);
        SqlSession session = config.getSqlSessionFactory().getSession();
        //Map<Object, T> sourcesMap = null;
        Map<Object, List<T>> sourcesMapList = null;
        for (IncludeSet include : includes)
        {
            NavigateData navigateData = include.getPropertyContext().getPropertyMetaData().getNavigateData();
            Class<?> navigateTargetType = navigateData.getNavigateTargetType();
            PropertyMetaData selfPropertyMetaData = targetClassMetaData.getPropertyMetaData(navigateData.getSelfPropertyName());
            PropertyMetaData targetPropertyMetaData = MetaDataCache.getMetaData(navigateTargetType).getPropertyMetaData(navigateData.getTargetPropertyName());
            PropertyMetaData includePropertyMetaData = include.getPropertyContext().getPropertyMetaData();

            if (sourcesMapList == null)
            {
                sourcesMapList = getMapList(selfPropertyMetaData);
            }

            switch (navigateData.getRelationType())
            {
                case OneToOne:
                    oneToOne(sourcesMapList, session, include, navigateTargetType, selfPropertyMetaData, targetPropertyMetaData, includePropertyMetaData);
                    break;
                case OneToMany:
                    oneToMany(sourcesMapList, session, include, navigateTargetType, selfPropertyMetaData, targetPropertyMetaData, includePropertyMetaData);
                    break;
                case ManyToOne:
                    manyToOne(sourcesMapList, session, include, navigateTargetType, selfPropertyMetaData, targetPropertyMetaData, includePropertyMetaData);
                    break;
                case ManyToMany:
                    manyToMany(sourcesMapList, session, include, navigateData, navigateTargetType, selfPropertyMetaData, targetPropertyMetaData, includePropertyMetaData);
                    break;
            }
        }
    }

    private void oneToOne(Map<Object, List<T>> sourcesMapList, SqlSession session, IncludeSet include, Class<?> navigateTargetType, PropertyMetaData selfPropertyMetaData, PropertyMetaData targetPropertyMetaData, PropertyMetaData includePropertyMetaData) throws InvocationTargetException, IllegalAccessException
    {
        // 查询目标表
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
        // 包一层，并选择字段
        QuerySqlBuilder warp = new QuerySqlBuilder(config, new SqlVirtualTableContext(mainSqlBuilder));
        warp.setSelect(new SqlPropertyContext(selfPropertyMetaData, 0), mainSqlBuilder.getTargetClass());

        querySqlBuilder.addWhere(new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlParensContext(new SqlVirtualTableContext(warp))));

        // 如果有额外条件就加入
        if (include.hasCond())
        {
            SqlContext cond = include.getCond();
            // 复杂条件
            if (cond instanceof SqlVirtualTableContext)
            {
                SqlVirtualTableContext virtualTableContext = (SqlVirtualTableContext) cond;
                // 替换
                querySqlBuilder = warp(querySqlBuilder, targetPropertyMetaData, navigateTargetType, virtualTableContext);
            }
            // 简易条件
            else
            {
                querySqlBuilder.addWhere(new SqlParensContext(cond));
            }
        }

        List<Object> values = new ArrayList<>();
        String sql = querySqlBuilder.getSqlAndValue(values);
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
            for (T t : sourcesMapList.get(key))
            {
                includePropertyMetaData.getSetter().invoke(t, value);
            }
        }
        round(include, navigateTargetType, objectMap.values(), querySqlBuilder);
    }

    private void oneToMany(Map<Object, List<T>> sourcesMapList, SqlSession session, IncludeSet include, Class<?> navigateTargetType, PropertyMetaData selfPropertyMetaData, PropertyMetaData targetPropertyMetaData, PropertyMetaData includePropertyMetaData) throws InvocationTargetException, IllegalAccessException
    {
        // 查询目标表
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
        // querySqlBuilder.addJoin(JoinType.LEFT, new SqlRealTableContext(mainSqlBuilder.getTargetClass()), new SqlBinaryContext(SqlOperator.EQ, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlPropertyContext(selfPropertyMetaData, 1)));
        // 包一层，并选择字段
        QuerySqlBuilder warp = new QuerySqlBuilder(config, new SqlVirtualTableContext(mainSqlBuilder));
        warp.setSelect(new SqlPropertyContext(selfPropertyMetaData, 0), mainSqlBuilder.getTargetClass());

        querySqlBuilder.addWhere(new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlParensContext(new SqlVirtualTableContext(warp))));

        // 如果有额外条件就加入
        if (include.hasCond())
        {
            SqlContext cond = include.getCond();
            // 复杂条件
            if (cond instanceof SqlVirtualTableContext)
            {
                SqlVirtualTableContext virtualTableContext = (SqlVirtualTableContext) cond;
                // 替换
                querySqlBuilder = warp(querySqlBuilder, targetPropertyMetaData, navigateTargetType, virtualTableContext);
            }
            // 简易条件
            else
            {
                querySqlBuilder.addWhere(new SqlParensContext(cond));
            }
        }

        List<Object> values = new ArrayList<>();
        String sql = querySqlBuilder.getSqlAndValue(values);
        System.out.println(sql);
        List<PropertyMetaData> mappingData = querySqlBuilder.getMappingData(null);
        // 查询从表数据，按key进行list归类的map构建
        Map<Object, List<Object>> targetMap = session.executeQuery(
                r -> ObjectBuilder.start(r, cast(navigateTargetType), mappingData, false).createMapList(targetPropertyMetaData.getColumn()),
                sql,
                values
        );
        // 一对多赋值
        for (Map.Entry<Object, List<Object>> objectListEntry : targetMap.entrySet())
        {
            Object key = objectListEntry.getKey();
            List<Object> value = objectListEntry.getValue();
            for (T t : sourcesMapList.get(key))
            {
                includePropertyMetaData.getSetter().invoke(t, value);
            }
        }
        round(include, navigateTargetType, targetMap.values(), querySqlBuilder);
    }

    private void manyToOne(Map<Object, List<T>> sourcesMapList, SqlSession session, IncludeSet include, Class<?> navigateTargetType, PropertyMetaData selfPropertyMetaData, PropertyMetaData targetPropertyMetaData, PropertyMetaData includePropertyMetaData) throws InvocationTargetException, IllegalAccessException
    {
        // 查询目标表
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
        // 包一层，并选择字段
        QuerySqlBuilder warp = new QuerySqlBuilder(config, new SqlVirtualTableContext(mainSqlBuilder));
        warp.setSelect(new SqlPropertyContext(selfPropertyMetaData, 0), mainSqlBuilder.getTargetClass());

        querySqlBuilder.addWhere(new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlParensContext(new SqlVirtualTableContext(warp))));

        // 如果有额外条件就加入
        if (include.hasCond())
        {
            SqlContext cond = include.getCond();
            // 复杂条件
            if (cond instanceof SqlVirtualTableContext)
            {
                SqlVirtualTableContext virtualTableContext = (SqlVirtualTableContext) cond;
                // 替换
                querySqlBuilder = warp(querySqlBuilder, targetPropertyMetaData, navigateTargetType, virtualTableContext);
            }
            // 简易条件
            else
            {
                querySqlBuilder.addWhere(new SqlParensContext(cond));
            }
        }

        List<Object> values = new ArrayList<>();
        String sql = querySqlBuilder.getSqlAndValue(values);
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
            List<T> ts = sourcesMapList.get(key);
            for (T t : ts)
            {
                includePropertyMetaData.getSetter().invoke(t, value);
            }
        }
        round(include, navigateTargetType, objectMap.values(), querySqlBuilder);
    }

    private void manyToMany(Map<Object, List<T>> sourcesMapList, SqlSession session, IncludeSet include, NavigateData navigateData, Class<?> navigateTargetType, PropertyMetaData selfPropertyMetaData, PropertyMetaData targetPropertyMetaData, PropertyMetaData includePropertyMetaData) throws InvocationTargetException, IllegalAccessException
    {
        Class<? extends IMappingTable> mappingTableType = navigateData.getMappingTableType();
        MetaData mappingTableMetadata = MetaDataCache.getMetaData(mappingTableType);
        String selfMappingPropertyName = navigateData.getSelfMappingPropertyName();
        PropertyMetaData selfMappingPropertyMetaData = mappingTableMetadata.getPropertyMetaData(selfMappingPropertyName);
        String targetMappingPropertyName = navigateData.getTargetMappingPropertyName();
        PropertyMetaData targetMappingPropertyMetaData = mappingTableMetadata.getPropertyMetaData(targetMappingPropertyName);
        // 查询目标表
        QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
        // join中间表
        querySqlBuilder.addJoin(JoinType.LEFT, new SqlRealTableContext(mappingTableType), new SqlBinaryContext(SqlOperator.EQ, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlPropertyContext(targetMappingPropertyMetaData, 1)));
        // 包一层，并选择字段
        QuerySqlBuilder warp = new QuerySqlBuilder(config, new SqlVirtualTableContext(mainSqlBuilder));
        warp.setSelect(new SqlPropertyContext(selfPropertyMetaData, 0), mainSqlBuilder.getTargetClass());

        querySqlBuilder.addWhere(new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(selfMappingPropertyMetaData, 1), new SqlParensContext(new SqlVirtualTableContext(warp))));

        // 增加上额外用于排序的字段
        SqlSelectorContext select = (SqlSelectorContext) querySqlBuilder.getSelect();
        querySqlBuilder.setSelect(select, navigateTargetType);
        select.getSqlContexts().add(new SqlPropertyContext(selfMappingPropertyMetaData, 1));

        // 如果有额外条件就加入
        if (include.hasCond())
        {
            SqlContext cond = include.getCond();
            // 复杂条件
            if (cond instanceof SqlVirtualTableContext)
            {
                SqlVirtualTableContext virtualTableContext = (SqlVirtualTableContext) cond;
                // 替换
                querySqlBuilder = warp(querySqlBuilder, selfMappingPropertyMetaData, navigateTargetType, virtualTableContext, new SqlPropertyContext(selfMappingPropertyMetaData, 0));
            }
            // 简易条件
            else
            {
                querySqlBuilder.addWhere(new SqlParensContext(cond));
            }
        }


        List<Object> values = new ArrayList<>();
        String sql = querySqlBuilder.getSqlAndValue(values);
        System.out.println(sql);
        List<PropertyMetaData> mappingData = querySqlBuilder.getMappingData(null);
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
        round(include, navigateTargetType, targetMap.values(), querySqlBuilder);
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

    private QuerySqlBuilder warp(QuerySqlBuilder querySqlBuilder, PropertyMetaData targetPropertyMetaData, Class<?> navigateTargetType, SqlVirtualTableContext virtualTableContext)
    {
        return warp(querySqlBuilder, targetPropertyMetaData, navigateTargetType, virtualTableContext, null);
    }

    private QuerySqlBuilder warp(QuerySqlBuilder querySqlBuilder, PropertyMetaData targetPropertyMetaData, Class<?> navigateTargetType, SqlVirtualTableContext virtualTableContext, SqlPropertyContext another)
    {
        List<SqlContext> orderBys = virtualTableContext.getSqlBuilder().getOrderBys();
        List<SqlContext> wheres = virtualTableContext.getSqlBuilder().getWheres();
        SqlLimitContext limit = virtualTableContext.getSqlBuilder().getLimit();
        if (!wheres.isEmpty())
        {
            querySqlBuilder.addWhere(new SqlParensContext(new SqlConditionsContext(wheres)));
        }
        // 包装一下窗口查询
        QuerySqlBuilder window = new QuerySqlBuilder(config, new SqlVirtualTableContext(querySqlBuilder));
        List<SqlContext> selects = new ArrayList<>(2);
        selects.add(new SqlConstString("*"));
        List<String> orderStr = new ArrayList<>();
        orderStr.add("ROW_NUMBER() OVER ( PARTITION BY ");
        List<SqlContext> newOrder = new ArrayList<>(orderBys);
        newOrder.add(0, new SqlPropertyContext(targetPropertyMetaData, 0));
        if (!orderBys.isEmpty())
        {
            orderStr.add(" ORDER BY ");
            for (int i = 0; i < orderBys.size(); i++)
            {
                if (i < orderBys.size() - 1) orderStr.add(",");
            }
        }
        orderStr.add(")");
        String rank = "-rank-";
        selects.add(new SqlAsNameContext(rank, new SqlFunctionsContext(orderStr, newOrder)));
        window.setSelect(new SqlSelectorContext(selects), navigateTargetType);
        // 最外层
        QuerySqlBuilder window2 = new QuerySqlBuilder(config, new SqlVirtualTableContext(window));
        if (another != null)
        {
            window2.setSelect(navigateTargetType);
            SqlSelectorContext selectorContext = (SqlSelectorContext) window2.getSelect();
            selectorContext.getSqlContexts().add(another);
        }
        else
        {
            window2.setSelect(querySqlBuilder.getSelect(), navigateTargetType);
        }
        if (limit != null)
        {
            long offset = limit.getOffset();
            long rows = limit.getRows();
            SqlConstString _rank_ = new SqlConstString(config.getDisambiguation().disambiguation(rank));
            SqlBinaryContext skip = null;
            SqlBinaryContext take;
            if (offset > 0)
            {
                skip = new SqlBinaryContext(SqlOperator.GT, _rank_, new SqlValueContext(offset));
                take = new SqlBinaryContext(SqlOperator.LE, _rank_, new SqlValueContext(offset + rows));
            }
            else
            {
                take = new SqlBinaryContext(SqlOperator.LE, _rank_, new SqlValueContext(rows));
            }
            SqlContext limitCond;
            if (skip != null)
            {
                limitCond = new SqlBinaryContext(SqlOperator.AND, skip, take);
            }
            else
            {
                limitCond = take;
            }
            window2.addWhere(limitCond);
        }
        return window2;
    }

    private void round(IncludeSet include, Class<?> navigateTargetType, Collection<?> sources, QuerySqlBuilder main, Object... os) throws InvocationTargetException, IllegalAccessException
    {
        if (!include.getIncludeSets().isEmpty())
        {
            new IncludeBuilder<>(config, cast(navigateTargetType), sources, include.getIncludeSets(), main).include();
        }
    }

    private void round(IncludeSet include, Class<?> navigateTargetType, Collection<List<Object>> sources, QuerySqlBuilder main) throws InvocationTargetException, IllegalAccessException
    {
        if (!include.getIncludeSets().isEmpty())
        {
            List<Object> collect = sources.stream().flatMap(o -> o.stream()).collect(Collectors.toList());
            new IncludeBuilder<>(config, cast(navigateTargetType), collect, include.getIncludeSets(), main).include();
        }
    }
}
