package io.github.kiryu1223.drink.core.expression.ext;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlFromExpression;
import io.github.kiryu1223.drink.core.expression.SqlRealTableExpression;
import io.github.kiryu1223.drink.core.expression.SqlTableExpression;

import java.util.List;

public class OracleSqlFromExpression extends SqlFromExpression
{
    public OracleSqlFromExpression(SqlTableExpression sqlTableExpression, int index)
    {
        super(sqlTableExpression, index);
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (sqlTableExpression instanceof SqlRealTableExpression)
        {
            return getSql(config);
        }
        else
        {
            return "FROM (" + sqlTableExpression.getSqlAndValue(config, values) + ") t" + index;
        }
    }

    @Override
    public String getSql(Config config)
    {
        String sql;
        if (sqlTableExpression instanceof SqlRealTableExpression)
        {
            sql = sqlTableExpression.getSql(config);
        }
        else
        {
            sql = "(" + sqlTableExpression.getSql(config) + ")";
        }
        return "FROM "+ sql + " t" + index;
    }
}
