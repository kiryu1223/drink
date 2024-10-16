package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlJoinExpression extends SqlExpression
{
    protected final JoinType joinType;
    protected final SqlTableExpression joinTable;
    protected final SqlExpression conditions;
    protected final int index;

    protected SqlJoinExpression(JoinType joinType, SqlTableExpression joinTable, SqlExpression conditions, int index)
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
        String t = "t" + index;
        return joinType.getJoin() + " " + (joinTable instanceof SqlRealTableExpression ? joinTable.getSqlAndValue(config, values) : "(" + joinTable.getSqlAndValue(config, values) + ")") + " AS " + config.getDisambiguation().disambiguation(t) + " ON " + conditions.getSqlAndValue(config, values);
    }

    @Override
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return (T) factory.join(joinType, joinTable.copy(config), conditions.copy(config), index);
    }
}
