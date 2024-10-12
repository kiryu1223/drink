package io.github.kiryu1223.drink.core.expression.ext.sqlite;

import io.github.kiryu1223.drink.core.expression.*;
import io.github.kiryu1223.drink.core.expression.ext.sqlserver.SqlServerLimitExpression;
import io.github.kiryu1223.drink.core.expression.ext.sqlserver.SqlServerQueryableExpression;
import io.github.kiryu1223.drink.core.expression.ext.sqlserver.SqlServerTypeExpression;

public class SqliteExpressionFactory extends SqlExpressionFactory
{
    @Override
    public SqlTypeExpression type(Class<?> c)
    {
        return new SqliteTypeExpression(c);
    }
}
