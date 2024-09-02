package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlJoinExpression extends SqlExpression
{
    private final JoinType joinType;
    private final SqlTableExpression joinTable;
    private final SqlExpression conditions;
    private final int index;

    public SqlJoinExpression(JoinType joinType, SqlTableExpression joinTable, SqlExpression conditions, int index)
    {
        this.joinType = joinType;
        this.joinTable = joinTable;
        this.conditions = conditions;
        this.index = index;
    }

    public SqlTableExpression getJoinTable()
    {
        return joinTable;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        return joinType.getJoin() + " " + (joinTable instanceof SqlRealTableExpression ? joinTable.getSqlAndValue(config, values) : "(" + joinTable.getSqlAndValue(config, values) + ")") + " AS t" + index + " ON " + conditions.getSqlAndValue(config, values);
    }

    @Override
    public String getSql(Config config)
    {
        return joinType.getJoin() + " " + (joinTable instanceof SqlRealTableExpression ? joinTable.getSql(config) : "(" + joinTable.getSql(config) + ")") + " AS t" + index + " ON " + conditions.getSql(config);
    }
}
