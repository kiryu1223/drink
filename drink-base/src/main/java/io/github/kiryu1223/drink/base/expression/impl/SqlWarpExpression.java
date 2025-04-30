package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlWarpExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

public class SqlWarpExpression implements ISqlWarpExpression {
    private final ISqlExpression expression;
    private final String filterId;

    public SqlWarpExpression(ISqlExpression expression, String filterId) {
        this.expression = expression;
        this.filterId = filterId;
    }

    @Override
    public ISqlExpression getExpression() {
        return expression;
    }

    @Override
    public String getFilterId() {
        return filterId;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        if (useIt()) {
            return expression.getSqlAndValue(config, values);
        }
        else {
            return "";
        }
    }
}
