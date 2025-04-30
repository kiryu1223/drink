package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.AsName;
import io.github.kiryu1223.drink.base.expression.ISqlDynamicColumnExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

public class SqlDynamicColumnExpression implements ISqlDynamicColumnExpression {
    private final String column;
    private final Class<?> type;
    private AsName tableAsName;

    public SqlDynamicColumnExpression(String column, Class<?> type, AsName tableAsName) {
        this.column = column;
        this.type = type;
        this.tableAsName = tableAsName;
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public AsName getTableAsName() {
        return tableAsName;
    }

    @Override
    public void setTableAsName(AsName tableAsName) {
        this.tableAsName = tableAsName;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        IDialect dialect = config.getDisambiguation();
        String columnName = dialect.disambiguation(column);
        if (tableAsName != null) {
            return dialect.disambiguation(tableAsName.getName()) + "." + columnName;
        }
        else {
            return columnName;
        }
    }
}
