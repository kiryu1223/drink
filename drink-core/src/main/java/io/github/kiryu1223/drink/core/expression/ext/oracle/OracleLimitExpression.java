package io.github.kiryu1223.drink.core.expression.ext.oracle;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlLimitExpression;

import java.util.List;

public class OracleLimitExpression extends SqlLimitExpression
{
    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (hasRowsOrOffset())
        {
            return String.format("OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", offset, rows);
        }
        return "";
    }
}
