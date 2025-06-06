package io.github.kiryu1223.drink.db.sqlserver;

import io.github.kiryu1223.drink.base.expression.ISqlTableExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableRefExpression;
import io.github.kiryu1223.drink.base.expression.impl.SqlFromExpression;
import io.github.kiryu1223.drink.base.util.DrinkUtil;

public class SQLServerFromExpression extends SqlFromExpression {
    protected SQLServerFromExpression(ISqlTableExpression sqlTableExpression, ISqlTableRefExpression tableRefExpression) {
        super(sqlTableExpression, tableRefExpression);
    }

    @Override
    public ISqlTableRefExpression getTableRefExpression() {
        if (pivotExpressions.isEmpty()) {
            return tableRefExpression;
        }
        return DrinkUtil.last(pivotExpressions).getTableRefExpression();
    }
}
