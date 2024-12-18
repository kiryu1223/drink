package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlConditionsExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlHavingExpression;

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
        System.out.println(condition);
        conditions.addCondition(condition);
    }

    @Override
    public ISqlConditionsExpression getConditions()
    {
        return conditions;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        if (isEmpty()) return "";
        return "HAVING " + getConditions().getSqlAndValue(config, values);
    }
}
