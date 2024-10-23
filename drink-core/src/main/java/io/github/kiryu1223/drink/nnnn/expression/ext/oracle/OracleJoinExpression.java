package io.github.kiryu1223.drink.nnnn.expression.ext.oracle;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.JoinType;
import io.github.kiryu1223.drink.nnnn.expression.*;

import java.util.List;

public class OracleJoinExpression extends SqlJoinExpression
{
    protected OracleJoinExpression(JoinType joinType, SqlTableExpression joinTable, ISqlExpression conditions, int index)
    {
        super(joinType, joinTable, conditions, index);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        return joinType.getJoin() + " " + (joinTable instanceof SqlRealTableExpression ? joinTable.getSqlAndValue(config, values) : "(" + joinTable.getSqlAndValue(config, values) + ")") + " t" + index + " ON " + conditions.getSqlAndValue(config, values);
    }
}
