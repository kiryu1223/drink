package io.github.kiryu1223.drink.core.expression.oracle;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlRealTableExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableExpression;
import io.github.kiryu1223.drink.base.expression.impl.SqlFromExpression;

import java.util.List;

public class OracleFromExpression extends SqlFromExpression
{
    public OracleFromExpression(ISqlTableExpression sqlTableExpression, int index)
    {
        super(sqlTableExpression, index);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        if (isEmptyTable()) return "FROM \"DUAL\"";
        String sql;
        if (sqlTableExpression instanceof ISqlRealTableExpression)
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
