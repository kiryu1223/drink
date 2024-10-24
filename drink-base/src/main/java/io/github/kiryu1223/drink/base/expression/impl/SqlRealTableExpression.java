package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.expression.ISqlRealTableExpression;

import java.util.List;

public class SqlRealTableExpression extends SqlTableExpression implements ISqlRealTableExpression
{
    private final Class<?> tableClass;

    SqlRealTableExpression(Class<?> tableClass)
    {
        this.tableClass = tableClass;
    }

    @Override
    public Class<?> getTableClass()
    {
        return tableClass;
    }
}
