package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlPivotExpression;
import io.github.kiryu1223.drink.base.expression.ISqlPivotsExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.List;

public class SqlPivotsExpression implements ISqlPivotsExpression {
    private final List<ISqlPivotExpression> pivots = new ArrayList<>();

    public SqlPivotsExpression() {
    }

    @Override
    public List<ISqlPivotExpression> getPivots() {
        return pivots;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        if (pivots.isEmpty()) return "";
        List<String> strings = new ArrayList<>();
        for (ISqlPivotExpression pivot : pivots) {
            strings.add(pivot.getSqlAndValue(config, values));
        }
        return String.join(" ", strings);
    }
}
