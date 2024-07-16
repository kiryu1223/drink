package io.github.kiryu1223.drink.starter;

import io.github.kiryu1223.drink.ext.DbType;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.sql.DataSource;
import java.util.Map;

@ConfigurationProperties(prefix = "drink")
public class DrinkProperties {
    private DbType dataBase = DbType.MySQL;
    private boolean printSql = true;
    private boolean printUseDs = true;
    private boolean printBatch = true;
    private Map<String, DataSourceProperties> dataSources;

    public DbType getDataBase() {
        return dataBase;
    }

    public void setDataBase(DbType dataBase) {
        this.dataBase = dataBase;
    }

    public boolean isPrintSql() {
        return printSql;
    }

    public void setPrintSql(boolean printSql) {
        this.printSql = printSql;
    }

    public Map<String, DataSourceProperties> getDataSources() {
        return dataSources;
    }

    public void setDataSources(Map<String, DataSourceProperties> dataSources) {
        this.dataSources = dataSources;
    }

    public boolean isPrintUseDs() {
        return printUseDs;
    }

    public void setPrintUseDs(boolean printUseDs) {
        this.printUseDs = printUseDs;
    }

    public boolean isPrintBatch() {
        return printBatch;
    }

    public void setPrintBatch(boolean printBatch) {
        this.printBatch = printBatch;
    }
}
