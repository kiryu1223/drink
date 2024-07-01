package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.expressionTree.expressions.OperatorType;

import java.util.List;

public abstract class SqlContext
{
    public abstract String getSqlAndValue(Config config, List<Object> values);
    public abstract String getSql(Config config);
    protected String getTableAsName(int index)
    {
        return "t" + index;
    }
}
