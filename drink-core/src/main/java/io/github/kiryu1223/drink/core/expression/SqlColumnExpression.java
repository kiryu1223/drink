package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.dialect.IDialect;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;

import java.util.List;

public class SqlColumnExpression extends SqlExpression
{
    private final PropertyMetaData propertyMetaData;
    private final int tableIndex;

    SqlColumnExpression(PropertyMetaData propertyMetaData, int tableIndex)
    {
        this.propertyMetaData = propertyMetaData;
        this.tableIndex = tableIndex;
    }

    public PropertyMetaData getPropertyMetaData()
    {
        return propertyMetaData;
    }

    public int getTableIndex()
    {
        return tableIndex;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return getSql(config);
    }

    @Override
    public String getSql(Config config)
    {
        IDialect dbConfig = config.getDisambiguation();
        return "t" + tableIndex + "." + dbConfig.disambiguation(propertyMetaData.getColumn());
    }
}
