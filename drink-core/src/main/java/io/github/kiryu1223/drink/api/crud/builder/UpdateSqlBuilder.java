package io.github.kiryu1223.drink.api.crud.builder;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.inter.IDBConfig;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.context.*;

import java.util.ArrayList;
import java.util.List;

public class UpdateSqlBuilder implements ISqlBuilder
{
    private final Config config;
    private final List<SqlJoinContext> joins = new ArrayList<>();
    private final List<SqlContext> sets = new ArrayList<>();
    private final List<SqlContext> wheres = new ArrayList<>();
    private Class<?> mainTable;

    public UpdateSqlBuilder(Config config)
    {
        this.config = config;
    }

    public void setMainTable(Class<?> mainTable)
    {
        this.mainTable = mainTable;
    }

    public void addJoin(Class<?> target, JoinType joinType, SqlTableContext tableContext, SqlContext onContext)
    {
        SqlJoinContext joinContext = new SqlJoinContext(
                joinType,
                new SqlAsTableNameContext(1 + joins.size(),
                        tableContext instanceof SqlRealTableContext
                                ? tableContext :
                                new SqlParensContext(tableContext)),
                onContext
        );
        joins.add(joinContext);
    }

    public void addSet(SqlContext set)
    {
        sets.add(set);
    }

    public void addWhere(SqlContext where)
    {
        wheres.add(where);
    }

    public boolean hasWhere()
    {
        return !wheres.isEmpty();
    }

    @Override
    public String getSql()
    {
        return makeUpdate() + makeJoin() + makeSet() + makeWhere();
    }

    @Override
    public String getSqlAndValue(List<Object> values)
    {
        return makeUpdate() + makeJoin(values) + makeSet(values) + makeWhere(values);
    }

    public Config getConfig()
    {
        return config;
    }

    private String makeUpdate()
    {
        MetaData metaData = MetaDataCache.getMetaData(mainTable);
        IDBConfig dbConfig = config.getDbConfig();
        return "UPDATE " + dbConfig.propertyDisambiguation(metaData.getTableName()) + " AS t0";
    }

    private String makeJoin(List<Object> values)
    {
        if (joins.isEmpty()) return "";
        List<String> joinStr = new ArrayList<>(joins.size());
        for (SqlContext context : joins)
        {
            joinStr.add(context.getSqlAndValue(config, values));
        }
        return " " + String.join(",", joinStr);
    }

    private String makeJoin()
    {
        if (joins.isEmpty()) return "";
        List<String> joinStr = new ArrayList<>(joins.size());
        for (SqlContext context : joins)
        {
            joinStr.add(context.getSql(config));
        }
        return " " + String.join(",", joinStr);
    }

    private String makeSet()
    {
        List<String> strings = new ArrayList<>();
        for (SqlContext set : sets)
        {
            strings.add(set.getSql(config));
        }
        return " SET " + String.join(",", strings);
    }

    private String makeSet(List<Object> values)
    {
        List<String> strings = new ArrayList<>();
        for (SqlContext set : sets)
        {
            strings.add(set.getSqlAndValue(config, values));
        }
        return " SET " + String.join(",", strings);
    }

    private String makeWhere()
    {
        if (wheres.isEmpty()) return "";
        List<String> whereStr = new ArrayList<>(wheres.size());
        for (SqlContext context : wheres)
        {
            whereStr.add(context.getSql(config));
        }
        return " WHERE " + String.join(" AND ", whereStr);
    }

    private String makeWhere(List<Object> values)
    {
        if (wheres.isEmpty()) return "";
        List<String> whereStr = new ArrayList<>(wheres.size());
        for (SqlContext context : wheres)
        {
            whereStr.add(context.getSqlAndValue(config, values));
        }
        return " WHERE " + String.join(" AND ", whereStr);
    }
}
