package io.github.kiryu1223.drink.nnnn.expression;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTemplateExpression;
import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.List;

public class SqlTemplateExpression implements ISqlTemplateExpression
{
    private final List<String> functions;
    private final List<? extends ISqlExpression> expressions;

    SqlTemplateExpression(List<String> functions, List<? extends ISqlExpression> expressions)
    {
        this.functions = functions;
        this.expressions = expressions;
    }

    @Override
    public List<String> getFunctions()
    {
        return functions;
    }

    @Override
    public List<? extends ISqlExpression> getExpressions()
    {
        return expressions;
    }
}
