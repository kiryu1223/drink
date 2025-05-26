package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlSingleValueExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableRefExpression;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

public class SubQueryValue implements ISqlColumnExpression
{
    // sub:xxx
    private final String keyName;
    private boolean keyNameUsed =false;
    private final int level;
    private final ISqlColumnExpression column;
    private Object value;

    public SubQueryValue(ISqlColumnExpression column, String keyName, int level)
    {
        this.keyName = keyName;
        this.column = column;
        this.level = level;
    }

    public boolean isKeyNameUsed() {
        return keyNameUsed;
    }

    public void setKeyNameUsed(boolean keyNameUsed) {
        this.keyNameUsed = keyNameUsed;
    }

    public String getKeyName()
    {
        return keyName;
    }

    public int getLevel()
    {
        return level;
    }

    public ISqlColumnExpression getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public FieldMetaData getFieldMetaData() {
        return column.getFieldMetaData();
    }

    @Override
    public ISqlTableRefExpression getTableRefExpression() {
        return column.getTableRefExpression();
    }

    @Override
    public void setTableRefExpression(ISqlTableRefExpression tableRefExpression) {
        column.setTableRefExpression(tableRefExpression);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        if (value == null) {
            return column.getSqlAndValue(config, values);
        }
        else {
            if (values != null) {
                values.add(new SqlValue(value));
            }
            return "?";
        }
    }
}
