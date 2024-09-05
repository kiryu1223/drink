package io.github.kiryu1223.drink.core.expression.ext.oracle;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.*;

import java.util.List;

public class OracleJoinExpression extends SqlJoinExpression
{
    protected OracleJoinExpression(JoinType joinType, SqlTableExpression joinTable, SqlExpression conditions, int index)
    {
        super(joinType, joinTable, conditions, index);
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return joinType.getJoin() + " " + (joinTable instanceof SqlRealTableExpression ? joinTable.getSqlAndValue(config, values) : "(" + joinTable.getSqlAndValue(config, values) + ")") + " t" + index + " ON " + conditions.getSqlAndValue(config, values);
    }

    @Override
    public String getSql(Config config)
    {
        return joinType.getJoin() + " " + (joinTable instanceof SqlRealTableExpression ? joinTable.getSql(config) : "(" + joinTable.getSql(config) + ")") + " t" + index + " ON " + conditions.getSql(config);
    }
}
