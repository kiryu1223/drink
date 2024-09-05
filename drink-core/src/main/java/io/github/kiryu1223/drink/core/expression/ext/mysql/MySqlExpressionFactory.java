package io.github.kiryu1223.drink.core.expression.ext.mysql;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.core.expression.SqlTypeExpression;

public class MySqlExpressionFactory extends SqlExpressionFactory
{
    public MySqlExpressionFactory(Config config)
    {
        super(config);
    }

    @Override
    public SqlTypeExpression type(Class<?> c)
    {
        return new MySqlTypeExpression(c);
    }
}
