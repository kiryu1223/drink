package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.ArrayList;
import java.util.List;

public class SqlFunctionExpression extends SqlExpression
{
    private final List<String> functions;
    private final List<? extends SqlExpression> expressions;

    SqlFunctionExpression(List<String> functions, List<? extends SqlExpression> expressions)
    {
        this.functions = functions;
        this.expressions = expressions;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < functions.size(); i++)
        {
            String function = functions.get(i);
            sb.append(function);
            if (i < expressions.size())
            {
                SqlExpression expression = expressions.get(i);
                sb.append(expression.getSqlAndValue(config, values));
            }
        }
        return sb.toString();
    }

    @Override
    public <T extends SqlExpression> T copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> newFunctions = new ArrayList<>(functions);
        List<? extends SqlExpression> newExpressions = new ArrayList<>(expressions);
        return (T) factory.function(newFunctions, newExpressions);
    }
}
