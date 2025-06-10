package io.github.kiryu1223.drink.db.sqlite;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableRefExpression;
import io.github.kiryu1223.drink.base.expression.impl.SqlUnPivotExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

public class SQLiteUnPivotExpression extends SqlUnPivotExpression {
    public SQLiteUnPivotExpression(ISqlQueryableExpression queryableExpression, String newNameColumnName, String newValueColumnName, Class<?> newValueColumnType, List<ISqlColumnExpression> transColumns, ISqlTableRefExpression tempRefExpression, ISqlTableRefExpression unpivotRefExpression) {
        super(queryableExpression, newNameColumnName, newValueColumnName, newValueColumnType, transColumns, tempRefExpression, unpivotRefExpression);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        // sqlite不支持unpivot
        return unionStyle(config, values);
    }
}
