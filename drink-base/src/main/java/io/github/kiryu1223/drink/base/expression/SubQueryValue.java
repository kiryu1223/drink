package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

public class SubQueryValue implements ISqlSingleValueExpression {
    private final String name;
    private final int level;
    private final FieldMetaData fieldMetaData;
    private Object value;

    public SubQueryValue(FieldMetaData fieldMetaData, int level) {
        this.level = level;
        this.fieldMetaData = fieldMetaData;
        this.name = fieldMetaData.getFieldName();
    }

    public String getValueName() {
        return "value:" + name;
    }

    public String getKeyName() {
        return "key:"+level+":" + name;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }



    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        if (values != null) {
            values.add(new SqlValue(value, fieldMetaData.getTypeHandler()));
        }
        return "?";
    }

    @Override
    public SubQueryValue copy(IConfig config) {
        SubQueryValue subQueryValue = new SubQueryValue(fieldMetaData, level);
        subQueryValue.setValue(value);
        return subQueryValue;
    }
}
