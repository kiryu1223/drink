package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlStarExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableRefExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

public class SqlStarExpression implements ISqlStarExpression
{
    private final ISqlTableRefExpression tableRefExpression;

    public SqlStarExpression(ISqlTableRefExpression tableRefExpression)
    {
        this.tableRefExpression = tableRefExpression;
    }

    @Override
    public ISqlTableRefExpression getTableRefExpression()
    {
        return tableRefExpression;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values)
    {
        if (tableRefExpression == null)
        {
            return "*";
        }
        else
        {
            return tableRefExpression.getDisPlayName()+".*";
        }
    }
}
