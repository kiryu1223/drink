package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.core.context.SqlContext;

public abstract class SqlTableExpression extends SqlExpression
{
    public abstract Class<?> getTableClass();
}
