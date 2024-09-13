package io.github.kiryu1223.drink.core.expression.ext.sqlserver;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlLimitExpression;

import java.util.List;

public class SqlServerLimitExpression extends SqlLimitExpression
{
    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (hasRowsAndOffset())
        {
            return String.format("OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", offset, rows);
        }
        return "";
    }
}
