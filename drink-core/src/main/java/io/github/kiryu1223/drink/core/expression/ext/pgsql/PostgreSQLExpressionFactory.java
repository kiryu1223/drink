package io.github.kiryu1223.drink.core.expression.ext.pgsql;

import io.github.kiryu1223.drink.core.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.core.expression.SqlTypeExpression;
import io.github.kiryu1223.drink.core.expression.ext.sqlite.SqliteTypeExpression;

public class PostgreSQLExpressionFactory extends SqlExpressionFactory
{
    @Override
    public SqlTypeExpression type(Class<?> c)
    {
        return new PostgreSQLTypeExpression(c);
    }
}
