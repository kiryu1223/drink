package io.github.kiryu1223.drink.core.expression.ext.sqlserver;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.*;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.exception.DrinkInvalidOperationException;

import java.util.ArrayList;
import java.util.List;

public class SqlServerQueryableExpression extends SqlQueryableExpression
{
    protected SqlServerQueryableExpression(SqlSelectExpression select, SqlFromExpression from, SqlJoinsExpression joins, SqlWhereExpression where, SqlGroupByExpression groupBy, SqlHavingExpression having, SqlOrderByExpression orderBy, SqlLimitExpression limit)
    {
        super(select, from, joins, where, groupBy, having, orderBy, limit);
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        List<String> strings = new ArrayList<>();
        makeSelect(strings, values, config);
        String fromSqlAndValue = from.getSqlAndValue(config, values);
        if (!fromSqlAndValue.isEmpty()) strings.add(fromSqlAndValue);
        String joinsSqlAndValue = joins.getSqlAndValue(config, values);
        if (!joinsSqlAndValue.isEmpty()) strings.add(joinsSqlAndValue);
        String whereSqlAndValue = where.getSqlAndValue(config, values);
        if (!whereSqlAndValue.isEmpty()) strings.add(whereSqlAndValue);
        String groupBySqlAndValue = groupBy.getSqlAndValue(config, values);
        if (!groupBySqlAndValue.isEmpty()) strings.add(groupBySqlAndValue);
        String havingSqlAndValue = having.getSqlAndValue(config, values);
        if (!havingSqlAndValue.isEmpty()) strings.add(havingSqlAndValue);
        String orderBySqlAndValue = orderBy.getSqlAndValue(config, values);
        if (!orderBySqlAndValue.isEmpty()) strings.add(orderBySqlAndValue);
        if (!from.isEmptyTable() && limit.hasRowsAndOffset() && orderBy.isEmpty())
        {
            addOrder(strings, config);
        }
        String limitSqlAndValue = limit.getSqlAndValue(config, values);
        if (!limitSqlAndValue.isEmpty()) strings.add(limitSqlAndValue);
        return String.join(" ", strings);
    }

//    @Override
//    public String getSqlAndValueAndFirst(Config config, List<Object> values)
//    {
//        List<String> strings = new ArrayList<>();
//        makeSelectTop1(strings, values, config);
//        String fromSqlAndValue = from.getSqlAndValue(config, values);
//        if (!fromSqlAndValue.isEmpty()) strings.add(fromSqlAndValue);
//        String joinsSqlAndValue = joins.getSqlAndValue(config, values);
//        if (!joinsSqlAndValue.isEmpty()) strings.add(joinsSqlAndValue);
//        String whereSqlAndValue = where.getSqlAndValue(config, values);
//        if (!whereSqlAndValue.isEmpty()) strings.add(whereSqlAndValue);
//        String groupBySqlAndValue = groupBy.getSqlAndValue(config, values);
//        if (!groupBySqlAndValue.isEmpty()) strings.add(groupBySqlAndValue);
//        String havingSqlAndValue = having.getSqlAndValue(config, values);
//        if (!havingSqlAndValue.isEmpty()) strings.add(havingSqlAndValue);
//        String orderBySqlAndValue = orderBy.getSqlAndValue(config, values);
//        if (!orderBySqlAndValue.isEmpty()) strings.add(orderBySqlAndValue);
//        return String.join(" ", strings);
//    }

    @Override
    public String getSql(Config config)
    {
        List<String> strings = new ArrayList<>();
        makeSelect(strings, null, config);
        String fromSql = from.getSql(config);
        if (!fromSql.isEmpty()) strings.add(fromSql);
        String joinsSql = joins.getSql(config);
        if (!joinsSql.isEmpty()) strings.add(joinsSql);
        String whereSql = where.getSql(config);
        if (!whereSql.isEmpty()) strings.add(whereSql);
        String groupBySql = groupBy.getSql(config);
        if (!groupBySql.isEmpty()) strings.add(groupBySql);
        String havingSql = having.getSql(config);
        if (!havingSql.isEmpty()) strings.add(havingSql);
        String orderBySql = orderBy.getSql(config);
        if (!orderBySql.isEmpty()) strings.add(orderBySql);
        if (!from.isEmptyTable() && limit.hasRowsAndOffset() && orderBy.isEmpty())
        {
            addOrder(strings, config);
        }
        String limitSql = limit.getSql(config);
        if (!limitSql.isEmpty()) strings.add(limitSql);
        return String.join(" ", strings);
    }

    private void addOrder(List<String> strings, Config config)
    {
        MetaData metaData = MetaDataCache.getMetaData(from.getSqlTableExpression().getTableClass());
        PropertyMetaData primary = metaData.getPrimary();
        if (primary == null)
        {
            throw new DrinkInvalidOperationException("MSSQL下进行的limit操作需要声明order by字段，或者指定一个主键");
        }
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        SqlOrderByExpression sqlOrderByExpression = factory.orderBy();
        sqlOrderByExpression.addOrder(factory.order(factory.column(primary)));
        strings.add(sqlOrderByExpression.getSql(config));
    }

    private void makeSelect(List<String> strings, List<Object> values, Config config)
    {
        List<String> result = new ArrayList<>();
        result.add("SELECT");
        if (select.isDistinct())
        {
            result.add("DISTINCT");
        }
        if (limit.onlyHasRows())
        {
            result.add("TOP(" + limit.getRows() + ")");
        }
        List<SqlExpression> columns = select.getColumns();
        List<String> columnsStr = new ArrayList<>(columns.size());
        if (values == null)
        {
            for (SqlExpression sqlExpression : columns)
            {
                columnsStr.add(sqlExpression.getSql(config));
            }
        }
        else
        {
            for (SqlExpression sqlExpression : columns)
            {
                columnsStr.add(sqlExpression.getSqlAndValue(config, values));
            }
        }
        result.add(String.join(",", columnsStr));
        strings.add(String.join(" ", result));
    }
}
