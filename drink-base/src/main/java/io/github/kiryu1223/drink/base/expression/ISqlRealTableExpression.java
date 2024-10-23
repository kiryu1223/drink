package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.metaData.MetaDataCache;

import java.util.List;

public interface ISqlRealTableExpression extends ISqlTableExpression
{
    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        String fullName = "";
        MetaData metaData = MetaDataCache.getMetaData(getTableClass());
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
    default ISqlRealTableExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.table(getTableClass());
    }
}
