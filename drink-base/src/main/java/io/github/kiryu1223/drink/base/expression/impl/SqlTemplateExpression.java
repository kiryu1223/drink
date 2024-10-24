package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTemplateExpression;

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

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getFunctions().size(); i++)
        {
            String function = getFunctions().get(i);
            sb.append(function);
            if (i < getExpressions().size())
            {
                ISqlExpression expression = getExpressions().get(i);
                sb.append(expression.getSqlAndValue(config, values));
            }
        }
        return sb.toString();
    }
}
