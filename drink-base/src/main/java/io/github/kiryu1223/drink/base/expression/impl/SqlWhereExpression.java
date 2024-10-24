package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlConditionsExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlWhereExpression;

import java.util.List;

public class SqlWhereExpression implements ISqlWhereExpression
{
    private final ISqlConditionsExpression conditions;

    SqlWhereExpression(ISqlConditionsExpression conditions)
    {
        this.conditions = conditions;
    }

    public void addCondition(ISqlExpression condition)
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

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        if (isEmpty()) return "";
        return "WHERE " + getConditions().getSqlAndValue(config, values);
    }
}
