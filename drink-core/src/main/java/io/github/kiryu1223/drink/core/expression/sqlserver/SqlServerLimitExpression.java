package io.github.kiryu1223.drink.core.expression.sqlserver;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.impl.SqlLimitExpression;

import java.util.List;

public class SqlServerLimitExpression extends SqlLimitExpression
{
    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        if (hasRowsAndOffset())
        {
            return String.format("OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", offset, rows);
        }
        return "";
    }
}
