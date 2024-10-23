package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlConditionsExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlHavingExpression;
import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlHavingExpression implements ISqlHavingExpression
{
    private final ISqlConditionsExpression conditions;

    public SqlHavingExpression(ISqlConditionsExpression conditions)
    {
        this.conditions = conditions;
    }

    public void addCond(ISqlExpression condition)
    {
        conditions.addCondition(condition);
    }

    @Override
    public ISqlConditionsExpression getConditions()
    {
        return conditions;
    }
}
