package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlSingleValueExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

public class SubQueryValue implements ISqlSingleValueExpression
{
    // sub:xxx
    private final String keyName;
    private final int level;
    private final ISqlSingleValueExpression singleValue;

    public SubQueryValue(ISqlSingleValueExpression singleValue, String keyName, int level)
    {
        this.keyName = keyName;
        this.singleValue = singleValue;
        this.level = level;
    }

    public String getKeyName()
    {
        return keyName;
    }

    public int getLevel()
    {
        return level;
    }

    @Override
    public Object getValue()
    {
        return singleValue.getValue();
    }

    @Override
    public void setValue(Object value)
    {
        singleValue.setValue(value);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values)
    {
        return singleValue.getSqlAndValue(config, values);
    }
}
