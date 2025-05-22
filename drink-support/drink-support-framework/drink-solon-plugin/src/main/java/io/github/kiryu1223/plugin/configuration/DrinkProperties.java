package io.github.kiryu1223.plugin.configuration;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.core.Option;
import org.noear.solon.annotation.Configuration;

@Configuration
public class DrinkProperties
{
    private DbType database = DbType.MySQL;
    private String dsName = "";
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

    public String getDsName()
    {
        return dsName;
    }

    public void setDsName(String dsName)
    {
        this.dsName = dsName;
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
                ", dsName='" + dsName + '\'' +
                ", printSql=" + printSql +
                ", printUseDs=" + printUseDs +
                ", printBatch=" + printBatch +
                '}';
    }
}
