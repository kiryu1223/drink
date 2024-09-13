package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.dialect.IDialect;

import java.util.List;

public class SqlAsExpression extends SqlExpression
{
    private final SqlExpression expression;
    private final String asName;

    SqlAsExpression(SqlExpression expression, String asName)
    {
        this.expression = expression;
        this.asName = asName;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        IDialect dialect = config.getDisambiguation();
        return expression.getSqlAndValue(config, values) + " AS " + dialect.disambiguation(asName);
    }

    @Override
    public SqlAsExpression copy(Config config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.as(expression.copy(config), asName);
    }
}
