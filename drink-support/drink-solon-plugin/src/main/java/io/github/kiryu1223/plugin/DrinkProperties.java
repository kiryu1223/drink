package io.github.kiryu1223.plugin;

import io.github.kiryu1223.drink.Option;
import io.github.kiryu1223.drink.ext.DbType;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

@Configuration
public class DrinkProperties
{
    private DbType database = DbType.MySQL;
    private String datasource = "";
    private boolean printSql = true;
    private boolean printUseDs = false;
    private boolean printBatch = false;

    public DbType getDataBase()
    {
        return database;
    }

    public void setDataBase(DbType database)
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

    public String getDatasource()
    {
        return datasource;
    }

    public void setDatasource(String datasource)
    {
        this.datasource = datasource;
    }

    public Option bulidOption()
    {
        Option option = new Option();
        option.setPrintSql(isPrintSql());
        option.setPrintUseDs(isPrintUseDs());
        option.setPrintBatch(isPrintBatch());
        return option;
    }

    @Override
    public String toString()
    {
        return "DrinkProperties{" +
                "database=" + database +
                ", datasource='" + datasource + '\'' +
                ", printSql=" + printSql +
                ", printUseDs=" + printUseDs +
                ", printBatch=" + printBatch +
                '}';
    }
}
