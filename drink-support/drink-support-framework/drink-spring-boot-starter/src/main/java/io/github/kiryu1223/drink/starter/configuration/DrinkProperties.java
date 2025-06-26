package io.github.kiryu1223.drink.starter.configuration;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.page.DefaultPager;
import io.github.kiryu1223.drink.base.page.Pager;
import io.github.kiryu1223.drink.core.Option;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "drink")
public class DrinkProperties {

    private boolean enable = false;
    private DbType database = DbType.MySQL;
    private boolean printSql = true;
    private boolean printBatch = false;
    private boolean ignoreUpdateNoWhere = false;
    private boolean ignoreDeleteNoWhere = false;
    private NameConversionType nameConversion = NameConversionType.LowerCamelCase;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public DbType getDatabase() {
        return database;
    }

    public void setDatabase(DbType database) {
        this.database = database;
    }

    public boolean isPrintSql() {
        return printSql;
    }

    public void setPrintSql(boolean printSql) {
        this.printSql = printSql;
    }

    public boolean isPrintBatch() {
        return printBatch;
    }

    public void setPrintBatch(boolean printBatch) {
        this.printBatch = printBatch;
    }

    public boolean isIgnoreUpdateNoWhere() {
        return ignoreUpdateNoWhere;
    }

    public void setIgnoreUpdateNoWhere(boolean ignoreUpdateNoWhere) {
        this.ignoreUpdateNoWhere = ignoreUpdateNoWhere;
    }

    public boolean isIgnoreDeleteNoWhere() {
        return ignoreDeleteNoWhere;
    }

    public void setIgnoreDeleteNoWhere(boolean ignoreDeleteNoWhere) {
        this.ignoreDeleteNoWhere = ignoreDeleteNoWhere;
    }

    public NameConversionType getNameConversion() {
        return nameConversion;
    }

    public void setNameConversion(NameConversionType nameConversion) {
        this.nameConversion = nameConversion;
    }

    public Option bulidOption() {
        Option option = new Option();
        option.setPrintSql(printSql);
        option.setPrintBatch(printBatch);
        option.setIgnoreUpdateNoWhere(ignoreUpdateNoWhere);
        option.setIgnoreDeleteNoWhere(ignoreDeleteNoWhere);
        return option;
    }
}
