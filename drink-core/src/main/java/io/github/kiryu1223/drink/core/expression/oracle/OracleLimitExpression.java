package io.github.kiryu1223.drink.core.expression.oracle;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.impl.SqlLimitExpression;

import java.util.List;

public class OracleLimitExpression extends SqlLimitExpression
{
    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
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
