package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

public class SubQueryValue implements ISqlColumnExpression
{
    // value:xxx
    private final String valueName;
    private boolean used =false;
    private final int level;
    private final ISqlColumnExpression column;
    private final FieldMetaData fieldMetaData;
    private Object value;

    public SubQueryValue(ISqlColumnExpression column, String valueName, int level)
    {
        this.valueName = valueName;
        this.column = column;
        this.level = level;
        this.fieldMetaData = column.getFieldMetaData();
    }

    public SubQueryValue(FieldMetaData fieldMetaData,String valueName, int level)
    {
        this.valueName = valueName;
        column=null;
        this.level = level;
        this.fieldMetaData = fieldMetaData;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getValueName()
    {
        return valueName;
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
    public ISqlColumnExpression copy(IConfig config)
    {
        if (column != null) {
            return ISqlColumnExpression.super.copy(config);
        }
        else {
            return new SubQueryValue(fieldMetaData,valueName,level);
        }
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        if (value == null) {
            return column.getSqlAndValue(config, values);
        }
        else {
            if (values != null) {
                values.add(new SqlValue(value,fieldMetaData.getTypeHandler()));
            }
            return "?";
        }
    }
}
