package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.dialect.IDialect;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;

import java.util.List;

public class SqlRealTableExpression extends SqlTableExpression
{
    private final Class<?> tableClass;

    SqlRealTableExpression(Class<?> tableClass)
    {
        this.tableClass = tableClass;
    }

    @Override
    public Class<?> getTableClass()
    {
        return tableClass;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        String fullName = "";
        MetaData metaData = MetaDataCache.getMetaData(tableClass);
        IDialect dbConfig = config.getDisambiguation();
        String schema = metaData.getSchema();
        if (!schema.isEmpty())
        {
            fullName += dbConfig.disambiguationTableName(schema) + ".";
        }
        fullName += dbConfig.disambiguationTableName(metaData.getTableName());
        return fullName;
    }

    @Override
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return (T) factory.table(tableClass);
    }
}
