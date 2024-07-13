package io.github.kiryu1223.drink.starter;

import io.github.kiryu1223.drink.ext.DbType;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "drink")
public class DrinkProperties
{
    private DbType dataBase = DbType.MySQL;
    private boolean printSql = true;

    public DbType getDataBase()
    {
        return dataBase;
    }

    public void setDataBase(DbType dataBase)
    {
        this.dataBase = dataBase;
    }

    public boolean isPrintSql()
    {
        return printSql;
    }

    public void setPrintSql(boolean printSql)
    {
        this.printSql = printSql;
    }
}
