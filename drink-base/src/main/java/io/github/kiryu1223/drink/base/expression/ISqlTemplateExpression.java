package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.ArrayList;
import java.util.List;

public interface ISqlTemplateExpression extends ISqlExpression
{
    List<String> getFunctions();

    List<? extends ISqlExpression> getExpressions();

    @Override
    default String getSqlAndValue(IConfig config, List<Object> values)
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

    @Override
    default ISqlTemplateExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> newFunctions = new ArrayList<>(getFunctions());
        List<? extends ISqlExpression> newExpressions = new ArrayList<>(getExpressions());
        return factory.template(newFunctions, newExpressions);
    }
}
