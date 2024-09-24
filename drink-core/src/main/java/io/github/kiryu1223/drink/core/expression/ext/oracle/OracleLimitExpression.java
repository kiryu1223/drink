package io.github.kiryu1223.drink.core.expression.ext.oracle;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlLimitExpression;

import java.util.List;

public class OracleLimitExpression extends SqlLimitExpression
{
    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (onlyHasRows())
        {
            return String.format("FETCH NEXT %d ROWS ONLY", rows);
        }
        else if (hasRowsAndOffset())
        {
            return String.format("OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", offset, rows);
        }
        return "";
    }
}
