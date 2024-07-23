package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.dialect.IDialect;
import io.github.kiryu1223.drink.core.context.*;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeleteSqlBuilder implements ISqlBuilder
{
    private final Config config;
    private final List<SqlJoinContext> joins = new ArrayList<>();
    private final List<Class<?>> orderClasses = new ArrayList<>();
    private final List<SqlContext> wheres = new ArrayList<>();
    private Class<?> fromTable;
    private Set<Integer> excludes = new HashSet<>();

    public DeleteSqlBuilder(Config config)
    {
        this.config = config;
    }

    public void setFromTable(Class<?> fromTable)
    {
        this.fromTable = fromTable;
        orderClasses.add(fromTable);
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
        orderClasses.add(target);
    }

    public void addExclude(Class<?> c)
    {
        excludes.add(orderClasses.indexOf(c));
    }

    public void addWhere(SqlContext where)
    {
        wheres.add(where);
    }

    @Override
    public Config getConfig()
    {
        return config;
    }

    public boolean hasWhere()
    {
        return !wheres.isEmpty();
    }

    @Override
    public String getSql()
    {
        return makeDelete() + makeJoin() + makeWhere();
    }

    @Override
    public String getSqlAndValue(List<Object> values)
    {
        return makeDelete() + makeJoin(values) + makeWhere(values);
    }

    private String makeDelete()
    {
        StringBuilder builder = new StringBuilder("DELETE");
        if (!excludes.isEmpty())
        {
            builder.append(" ");
            List<String> strings = new ArrayList<>(excludes.size());
            for (int index : excludes)
            {
                if (index != -1)
                {
                    strings.add("t" + index);
                }
            }
            builder.append(String.join(",", strings));
        }
        IDialect dbConfig = config.getDisambiguation();
        MetaData metaData = MetaDataCache.getMetaData(fromTable);
        return builder.append(" FROM ").append(dbConfig.disambiguation(metaData.getTableName())).append(" AS t0").toString();
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
