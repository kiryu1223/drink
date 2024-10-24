package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.metaData.PropertyMetaData;

import java.util.List;

public interface ISqlColumnExpression extends ISqlExpression
{
    PropertyMetaData getPropertyMetaData();

    int getTableIndex();

    @Override
    default ISqlColumnExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.column(getPropertyMetaData(), getTableIndex());
    }
}
