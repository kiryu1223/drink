package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlGroupRef;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

public class SqlGroupRef implements ISqlGroupRef
{
    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values)
    {
        return "";
    }

    @Override
    public <T extends ISqlExpression> T copy(IConfig config)
    {
        return null;
    }
}
