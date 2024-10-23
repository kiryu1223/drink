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
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        if (isEmptyTable()) return "";
        String sql;
        if (getSqlTableExpression() instanceof ISqlRealTableExpression)
        {
            sql = getSqlTableExpression().getSqlAndValue(config, values);
        }
        else
        {
            sql = "(" + getSqlTableExpression().getSqlAndValue(config, values) + ")";
        }
        String t = "t" + getIndex();
        return "FROM " + sql + " AS " + config.getDisambiguation().disambiguation(t);
    }

    @Override
    default ISqlFromExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.from(getSqlTableExpression().copy(config), getIndex());
    }
}
