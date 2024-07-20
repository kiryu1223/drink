package io.github.kiryu1223.drink.starter.configuration;

import io.github.kiryu1223.drink.Option;
import io.github.kiryu1223.drink.ext.DbType;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "drink")
public class DrinkProperties
{
    private DbType database = DbType.MySQL;
    private boolean printSql = true;
    private boolean printUseDs = false;
    private boolean printBatch = false;

    public DbType getDatabase()
    {
        return database;
    }

    public void setDatabase(DbType database)
    {
        this.database = database;
    }

    public boolean isPrintSql()
    {
        return printSql;
    }

    public void setPrintSql(boolean printSql)
    {
        this.printSql = printSql;
    }

    public boolean isPrintUseDs()
    {
        return printUseDs;
    }

    public void setPrintUseDs(boolean printUseDs)
    {
        this.printUseDs = printUseDs;
    }

    public boolean isPrintBatch()
    {
        return printBatch;
    }

    public void setPrintBatch(boolean printBatch)
    {
        this.printBatch = printBatch;
    }

    public Option bulidOption()
    {
        Option option = new Option();
        option.setPrintSql(printSql);
        option.setPrintUseDs(printUseDs);
        option.setPrintBatch(printBatch);
        return option;
    }
}