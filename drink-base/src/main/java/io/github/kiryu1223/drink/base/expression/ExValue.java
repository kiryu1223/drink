package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.util.ArrayList;
import java.util.List;

public class ExValue {
    private final FieldMetaData fieldMetaData;
    private final List<Object> values = new ArrayList<>();

    public ExValue(FieldMetaData fieldMetaData) {
        this.fieldMetaData = fieldMetaData;
    }

    public FieldMetaData getFieldMetaData() {
        return fieldMetaData;
    }

    public List<Object> getValues() {
        return values;
    }

    public void addValue(Object value) {
        values.add(value);
    }
}
