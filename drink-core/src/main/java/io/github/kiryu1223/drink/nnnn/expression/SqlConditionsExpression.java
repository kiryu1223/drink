package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlConditionsExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlConditionsExpression implements ISqlConditionsExpression
{
    private final List<ISqlExpression> conditions = new ArrayList<>();

    @Override
    public List<ISqlExpression> getConditions()
    {
        return conditions;
    }
}
