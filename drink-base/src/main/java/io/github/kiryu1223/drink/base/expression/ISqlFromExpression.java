package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.metaData.MetaDataCache;

import java.util.List;

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
