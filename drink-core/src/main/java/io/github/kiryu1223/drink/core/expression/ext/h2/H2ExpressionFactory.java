package io.github.kiryu1223.drink.core.expression.ext.h2;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.core.expression.SqlTypeExpression;

public class H2ExpressionFactory extends SqlExpressionFactory
{
    @Override
    public SqlTypeExpression type(Class<?> c)
    {
        return new H2TypeExpression(c);
    }
}
