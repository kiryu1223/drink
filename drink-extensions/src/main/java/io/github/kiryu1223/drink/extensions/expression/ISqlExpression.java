package io.github.kiryu1223.drink.extensions.expression;

import io.github.kiryu1223.drink.extensions.IConfig;

import java.util.List;

public interface ISqlExpression
{
    String getSqlAndValue(IConfig config, List<Object> values);

    default String getSql(IConfig config)
    {
        return getSqlAndValue(config, null);
    }

    <T extends ISqlExpression> T copy(IConfig config);
}
