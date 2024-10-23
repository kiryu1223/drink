package io.github.kiryu1223.drink.nnnn.expression.ext.oracle;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.nnnn.expression.SqlFromExpression;
import io.github.kiryu1223.drink.nnnn.expression.SqlRealTableExpression;
import io.github.kiryu1223.drink.nnnn.expression.SqlTableExpression;

import java.util.List;

public class OracleFromExpression extends SqlFromExpression
{
    public OracleFromExpression(SqlTableExpression sqlTableExpression, int index)
    {
        super(sqlTableExpression, index);
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        if (isEmptyTable()) return "FROM \"DUAL\"";
        String sql;
        if (sqlTableExpression instanceof SqlRealTableExpression)
        {
            sql = sqlTableExpression.getSqlAndValue(config, values);
        }
        else
        {
            sql = "(" + sqlTableExpression.getSqlAndValue(config, values) + ")";
        }
        return "FROM " + sql + " t" + index;
    }
}
