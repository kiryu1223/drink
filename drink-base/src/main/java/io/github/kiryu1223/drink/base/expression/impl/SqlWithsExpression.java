package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlWithExpression;
import io.github.kiryu1223.drink.base.expression.ISqlWithsExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.List;

public class SqlWithsExpression implements ISqlWithsExpression {
    protected final List<ISqlWithExpression> withExpressions = new ArrayList<>();

    @Override
    public List<ISqlWithExpression> getWiths() {
        return withExpressions;
    }

    @Override
    public void addWith(ISqlWithExpression withExpression) {
        withExpressions.add(withExpression);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        if (withExpressions.isEmpty()) return "";
        List<String> strings = new ArrayList<>(withExpressions.size());
        for (ISqlWithExpression withExpression : withExpressions) {
            strings.add(withExpression.getSqlAndValue(config, values));
        }
        return "WITH " + String.join(",", strings);
    }
}
