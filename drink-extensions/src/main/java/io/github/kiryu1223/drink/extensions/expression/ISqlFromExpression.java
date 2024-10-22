package io.github.kiryu1223.drink.extensions.expression;


import io.github.kiryu1223.drink.extensions.IConfig;
import io.github.kiryu1223.drink.extensions.metaData.MetaData;
import io.github.kiryu1223.drink.extensions.metaData.MetaDataCache;

public interface ISqlFromExpression extends ISqlExpression
{
    ISqlTableExpression getSqlTableExpression();

    default boolean isEmptyTable()
    {
        Class<?> tableClass = getSqlTableExpression().getTableClass();
        MetaData metaData = MetaDataCache.getMetaData(tableClass);
        return metaData.isEmptyTable();
    }

    int getIndex();

    @Override
    default ISqlFromExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.from(getSqlTableExpression().copy(config), getIndex());
    }
}
