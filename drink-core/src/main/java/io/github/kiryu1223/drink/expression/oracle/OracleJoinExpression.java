package io.github.kiryu1223.drink.expression.oracle;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlRealTableExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableExpression;
import io.github.kiryu1223.drink.base.expression.JoinType;
import io.github.kiryu1223.drink.base.expression.impl.SqlJoinExpression;

import java.util.List;

public class OracleJoinExpression extends SqlJoinExpression
{
    protected OracleJoinExpression(JoinType joinType, ISqlTableExpression joinTable, ISqlExpression conditions, int index)
    {
        super(joinType, joinTable, conditions, index);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        return joinType.getJoin() + " " + (joinTable instanceof ISqlRealTableExpression ? joinTable.getSqlAndValue(config, values) : "(" + joinTable.getSqlAndValue(config, values) + ")") + " t" + index + " ON " + conditions.getSqlAndValue(config, values);
    }
}
