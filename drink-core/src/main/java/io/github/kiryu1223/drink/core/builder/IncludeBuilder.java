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
        Map<Object, T> sourcesMap = null;
        Map<Object, List<T>> sourcesMapList = null;
        for (IncludeSet include : includes)
        {
            NavigateData navigateData = include.getPropertyContext().getPropertyMetaData().getNavigateData();
            Class<?> navigateTargetType = navigateData.getNavigateTargetType();
            PropertyMetaData selfPropertyMetaData = targetClassMetaData.getPropertyMetaData(navigateData.getSelfPropertyName());
            PropertyMetaData targetPropertyMetaData = MetaDataCache.getMetaData(navigateTargetType).getPropertyMetaData(navigateData.getTargetPropertyName());
            PropertyMetaData includePropertyMetaData = include.getPropertyContext().getPropertyMetaData();

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
                // 查询目标表
                QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
                // join自身表
                querySqlBuilder.addJoin(JoinType.LEFT, new SqlRealTableContext(mainSqlBuilder.getTargetClass()), new SqlBinaryContext(SqlOperator.EQ, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlPropertyContext(selfPropertyMetaData, 1)));
                // 包一层，并选择字段
                QuerySqlBuilder warp = new QuerySqlBuilder(config, new SqlVirtualTableContext(mainSqlBuilder));
                warp.setSelect(new SqlPropertyContext(selfPropertyMetaData, 0), mainSqlBuilder.getTargetClass());

                SqlBinaryContext condition = new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(selfPropertyMetaData, 0), new SqlParensContext(new SqlVirtualTableContext(warp)));

                // 如果有额外条件就加入
                if (include.hasCond())
                {
                    SqlContext cond = include.getCond();
                    SqlBinaryContext exCondition = new SqlBinaryContext(SqlOperator.AND, condition, new SqlParensContext(cond));
                    querySqlBuilder.addWhere(exCondition);
                }
                else
                {
                    querySqlBuilder.addWhere(condition);
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
                    T t = sourcesMap.get(key);
                    includePropertyMetaData.getSetter().invoke(t, value);
                }
            }

//            SELECT# rank仅作为where条件，非必须品
//              # t0.`-rank-`,
//              t0.`emp_no`,
//              t0.`from_date`,
//              t0.`salary`,
//              t0.`to_date`
//            FROM
//                    (
//                            SELECT
//                             *,
//                            ROW_NUMBER() OVER ( PARTITION BY t0.emp_no ) AS `-rank-`
//            FROM
//                    (
//                            SELECT
//                            t0.`emp_no`,
//                            t0.`from_date`,
//                            t0.`salary`,
//                            t0.`to_date`
//                            FROM
//                            `salaries` AS t0
//                            LEFT JOIN employees AS t1 ON t0.emp_no = t1.emp_no
//                            WHERE
//                            t1.emp_no IN ( SELECT t0.emp_no FROM ( SELECT * FROM employees AS t0 LIMIT 10 ) AS t0 )
//                    ) AS t0
//	                ) AS t0
//            WHERE
//            t0.`-rank-` <= 10
            // 一对多情况
            // 主表的字段(self)与多个从表的字段(target)对应
            else if (navigateData.getRelationType() == RelationType.OneToMany)
            {
                // 遍历结果提取key映射到map
                if (sourcesMap == null)
                {
                    sourcesMap = getMap(selfPropertyMetaData);
                }
                // 查询目标表
                QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
                // join自身表
                querySqlBuilder.addJoin(JoinType.LEFT, new SqlRealTableContext(mainSqlBuilder.getTargetClass()), new SqlBinaryContext(SqlOperator.EQ, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlPropertyContext(selfPropertyMetaData, 1)));
                // 包一层，并选择字段
                QuerySqlBuilder warp = new QuerySqlBuilder(config, new SqlVirtualTableContext(mainSqlBuilder));
                warp.setSelect(new SqlPropertyContext(selfPropertyMetaData, 0), mainSqlBuilder.getTargetClass());

                SqlBinaryContext condition = new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(targetPropertyMetaData, 1), new SqlParensContext(new SqlVirtualTableContext(warp)));
                querySqlBuilder.addWhere(condition);

                // 如果有额外条件就加入
                if (include.hasCond())
                {
                    SqlContext cond = include.getCond();
                    // 复杂条件
                    if (cond instanceof SqlVirtualTableContext)
                    {
                        SqlVirtualTableContext virtualTableContext = (SqlVirtualTableContext) cond;
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
                        window2.setSelect(querySqlBuilder.getSelect(), navigateTargetType);
                        if (limit != null)
                        {
                            SqlConstString _rank_ = new SqlConstString(config.getDisambiguation().disambiguation(rank));
                            SqlBinaryContext skip = new SqlBinaryContext(SqlOperator.GT, _rank_, new SqlValueContext(limit.getOffset()));
                            SqlBinaryContext take = new SqlBinaryContext(SqlOperator.LE, _rank_, new SqlValueContext(limit.getRows()));
                            window2.addWhere(new SqlParensContext(new SqlBinaryContext(SqlOperator.AND, skip, take)));
                        }
                        // 替换
                        querySqlBuilder = window2;
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
                // 查询目标表
                QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
                // join自身表
                querySqlBuilder.addJoin(JoinType.LEFT, new SqlRealTableContext(mainSqlBuilder.getTargetClass()), new SqlBinaryContext(SqlOperator.EQ, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlPropertyContext(selfPropertyMetaData, 1)));
                // 包一层，并选择字段
                QuerySqlBuilder warp = new QuerySqlBuilder(config, new SqlVirtualTableContext(mainSqlBuilder));
                warp.setSelect(new SqlPropertyContext(selfPropertyMetaData, 0), mainSqlBuilder.getTargetClass());

                SqlBinaryContext condition = new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlParensContext(new SqlVirtualTableContext(warp)));
                querySqlBuilder.addWhere(condition);
                // 如果有额外条件就加入
                if (include.hasCond())
                {
                    SqlContext cond = include.getCond();
                    // 复杂条件
                    if (cond instanceof SqlVirtualTableContext)
                    {
                        SqlVirtualTableContext virtualTableContext = (SqlVirtualTableContext) cond;
                        QuerySqlBuilder sqlBuilder = virtualTableContext.getSqlBuilder();
                        List<SqlContext> wheres = sqlBuilder.getWheres();
                        List<SqlContext> orderBys = sqlBuilder.getOrderBys();
                        SqlLimitContext limit = sqlBuilder.getLimit();
                        if (!wheres.isEmpty())
                        {
                            querySqlBuilder.addWhere(new SqlParensContext(new SqlConditionsContext(wheres)));
                        }
                        if (!orderBys.isEmpty())
                        {
                            querySqlBuilder.addOrderBy(orderBys);
                        }
                        if (limit != null)
                        {
                            querySqlBuilder.setLimit(limit);
                        }
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
                // 查询目标表
                QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder(config, new SqlRealTableContext(navigateTargetType));
                // join中间表
                querySqlBuilder.addJoin(JoinType.LEFT, new SqlRealTableContext(mappingTableType), new SqlBinaryContext(SqlOperator.EQ, new SqlPropertyContext(targetPropertyMetaData, 0), new SqlPropertyContext(targetMappingPropertyMetaData, 1)));
                // 包一层，并选择字段
                QuerySqlBuilder warp = new QuerySqlBuilder(config, new SqlVirtualTableContext(mainSqlBuilder));
                warp.setSelect(new SqlPropertyContext(selfPropertyMetaData, 0), mainSqlBuilder.getTargetClass());

                SqlBinaryContext condition = new SqlBinaryContext(SqlOperator.IN, new SqlPropertyContext(selfMappingPropertyMetaData, 1), new SqlParensContext(new SqlVirtualTableContext(warp)));
                // 如果有额外条件就加入
                if (include.hasCond())
                {
                    SqlContext cond = include.getCond();
                    SqlBinaryContext exCondition = new SqlBinaryContext(SqlOperator.AND, condition, new SqlParensContext(cond));
                    querySqlBuilder.addWhere(exCondition);
                }
                else
                {
                    querySqlBuilder.addWhere(condition);
                }

                SqlSelectorContext select = (SqlSelectorContext) querySqlBuilder.getSelect();
                querySqlBuilder.setSelect(select, navigateTargetType);
                select.getSqlContexts().add(new SqlPropertyContext(selfMappingPropertyMetaData, 1));
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
