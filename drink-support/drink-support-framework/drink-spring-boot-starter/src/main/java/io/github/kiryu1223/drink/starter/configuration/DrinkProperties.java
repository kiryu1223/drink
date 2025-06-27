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
    private boolean printValues = true;
    private boolean printTotal = true;
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

    public boolean isPrintValues()
    {
        return printValues;
    }

    public void setPrintValues(boolean printValues)
    {
        this.printValues = printValues;
    }

    public boolean isPrintTotal()
    {
        return printTotal;
    }

    public void setPrintTotal(boolean printTotal)
    {
        this.printTotal = printTotal;
    }

    public Option bulidOption() {
        Option option = new Option();
        option.setPrintSql(printSql);
        option.setPrintValues(printValues);
        option.setPrintTotal(printTotal);
        option.setIgnoreUpdateNoWhere(ignoreUpdateNoWhere);
        option.setIgnoreDeleteNoWhere(ignoreDeleteNoWhere);
        return option;
    }
}
