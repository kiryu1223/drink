package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.inter.IDBConfig;

import java.lang.reflect.Method;

public class ColumnMetaData
{
    private final String columnName;
    private final Method columnSetter;

    public ColumnMetaData(String columnName, Method columnSetter, Config config)
    {
        //IDBConfig dbConfig = config.getDbConfig();
        this.columnName = columnName;
        this.columnSetter = columnSetter;
    }

    public String getColumnName()
    {
        return columnName;
    }

    public Method getColumnSetter()
    {
        return columnSetter;
    }
}
