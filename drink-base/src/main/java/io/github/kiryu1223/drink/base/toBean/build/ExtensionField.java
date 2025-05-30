package io.github.kiryu1223.drink.base.toBean.build;

import io.github.kiryu1223.drink.base.metaData.FieldMetaData;

public class ExtensionField {
    private final FieldMetaData fieldMetaData;
    private final Object value;

    public ExtensionField(FieldMetaData fieldMetaData, Object value) {
        this.fieldMetaData = fieldMetaData;
        this.value = value;
    }

    public FieldMetaData getFieldMetaData() {
        return fieldMetaData;
    }

    public Object getValue() {
        return value;
    }
}
