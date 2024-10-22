package io.github.kiryu1223.drink.extensions.expression;


import io.github.kiryu1223.drink.extensions.IConfig;
import io.github.kiryu1223.drink.extensions.metaData.PropertyMetaData;

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
