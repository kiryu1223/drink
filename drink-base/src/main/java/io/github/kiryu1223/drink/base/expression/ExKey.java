package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.metaData.FieldMetaData;

import java.util.ArrayList;
import java.util.List;

public class ExKey {
    private final FieldMetaData fieldMetaData;
    private final String keyName;

    public ExKey(FieldMetaData fieldMetaData, String keyName) {
        this.fieldMetaData = fieldMetaData;
        this.keyName = keyName;
    }

    public FieldMetaData getFieldMetaData() {
        return fieldMetaData;
    }

    public String getKeyName() {
        return keyName;
    }
}
