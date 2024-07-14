package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.builder.PropertyMetaData;

import java.util.List;

public class SqlSetContext extends SqlContext
{
    private final SqlPropertyContext propertyContext;
    private final SqlContext value;

    public SqlSetContext(SqlPropertyContext propertyContext, SqlContext value)
    {
        this.propertyContext = propertyContext;
        this.value = value;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        String set = propertyContext.getSqlAndValue(config, values) + " = ";
        PropertyMetaData propertyMetaData = propertyContext.getPropertyMetaData();
        if (propertyMetaData.isHasConverter() && value instanceof SqlValueContext)
        {
            SqlValueContext sqlValueContext = (SqlValueContext) value;
            return set + sqlValueContext.getSqlAndValue(config, values, propertyMetaData.getConverter(), propertyMetaData);
        }
        else
        {
            return set + value.getSqlAndValue(config, values);
        }
    }

    @Override
    public String getSql(Config config)
    {
        return propertyContext.getSql(config) + " = " + value.getSql(config);
    }
}
