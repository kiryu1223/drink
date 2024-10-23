package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;

import java.util.List;

public interface ISqlAsExpression extends ISqlExpression
{
    ISqlExpression getExpression();

    String getAsName();

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
    {
        IDialect dialect = config.getDisambiguation();
        return getExpression().getSqlAndValue(config, values) + " AS " + dialect.disambiguation(getAsName());
    }

    @Override
    default ISqlAsExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.as(getExpression().copy(config), getAsName());
    }
}
