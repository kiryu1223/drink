package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.ISqlDynamicColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableRefExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

public class SqlDynamicColumnExpression implements ISqlDynamicColumnExpression {
    private final String column;
    private final Class<?> type;
    private final ISqlTableRefExpression tableRefExpression;
    private boolean useDialect = true;

    protected SqlDynamicColumnExpression(String column, Class<?> type, ISqlTableRefExpression tableRefExpression) {
        this.column = column;
        this.type = type;
        this.tableRefExpression = tableRefExpression;
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public Class<?> getColumnType() {
        return type;
    }

    @Override
    public ISqlTableRefExpression getTableRefExpression() {
        return tableRefExpression;
    }

    @Override
    public boolean useDialect() {
        return useDialect;
    }

    @Override
    public void setUseDialect(boolean use) {
        useDialect = use;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        IDialect dialect = config.getDisambiguation();
        String columnName = useDialect ? dialect.disambiguation(column) : column;
        return dialect.disambiguation(tableRefExpression.getDisPlayName()) + "." + columnName;
    }
}
