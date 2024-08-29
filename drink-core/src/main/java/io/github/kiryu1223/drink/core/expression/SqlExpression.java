package io.github.kiryu1223.drink.core.expression;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public abstract class SqlExpression
{
    public abstract String getSqlAndValue(Config config, List<Object> values);

    public abstract String getSql(Config config);
}
