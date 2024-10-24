package io.github.kiryu1223.drink.base.expression;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.PropertyMetaData;

import java.util.List;

public interface ISqlSetExpression extends ISqlExpression
{
    ISqlColumnExpression getColumn();

    ISqlExpression getValue();

    @Override
    default ISqlSetExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.set(getColumn().copy(config), getValue().copy(config));
    }
}
