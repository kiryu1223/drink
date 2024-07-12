package io.github.kiryu1223.drink.api.crud.builder;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.inter.IDBConfig;
import io.github.kiryu1223.drink.core.builder.MetaData;
import io.github.kiryu1223.drink.core.builder.MetaDataCache;
import io.github.kiryu1223.drink.core.context.SqlContext;

import java.util.ArrayList;
import java.util.List;

public class DeleteSqlBuilder implements ISqlBuilder
{
    private final Config config;
    private final List<SqlContext> wheres = new ArrayList<>();
    private Class<?> deleteTable;

    public DeleteSqlBuilder(Config config)
    {
        this.config = config;
    }

    public void setDeleteTable(Class<?> deleteTable)
    {
        this.deleteTable = deleteTable;
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
        return makeDelete() + makeWhere();
    }

    @Override
    public String getSqlAndValue(List<Object> values)
    {
        return makeDelete() + makeWhere(values);
    }

    private String makeDelete()
    {
        IDBConfig dbConfig = config.getDbConfig();
        MetaData metaData = MetaDataCache.getMetaData(deleteTable);
        return "DELETE FROM " + dbConfig.propertyDisambiguation(metaData.getTableName()) + " AS t0";
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
