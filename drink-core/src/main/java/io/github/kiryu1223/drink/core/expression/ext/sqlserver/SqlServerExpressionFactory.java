package io.github.kiryu1223.drink.core.expression.ext.sqlserver;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.core.expression.SqlTypeExpression;

public class SqlServerExpressionFactory extends SqlExpressionFactory
{
    public SqlServerExpressionFactory(Config config)
    {
        super(config);
    }

    @Override
    public SqlTypeExpression type(Class<?> c)
    {
        return new SqlServerTypeExpression(c);
    }
}
