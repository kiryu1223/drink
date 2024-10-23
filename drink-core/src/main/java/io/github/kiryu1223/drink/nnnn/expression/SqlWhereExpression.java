package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlConditionsExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlWhereExpression;
import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlWhereExpression implements ISqlWhereExpression
{
    private final ISqlConditionsExpression conditions;

    SqlWhereExpression(ISqlConditionsExpression conditions)
    {
        this.conditions = conditions;
    }

    public void addCond(ISqlExpression condition)
    {
        conditions.getConditions().add(condition);
    }

    public boolean isEmpty()
    {
        return conditions.isEmpty();
    }

    public ISqlConditionsExpression getConditions()
    {
        return conditions;
    }
}
