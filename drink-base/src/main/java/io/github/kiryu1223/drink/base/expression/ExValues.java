package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.metaData.FieldMetaData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExValues {
    // key = value:xx
    private final Map<String, FieldMetaData> exValueMap=new HashMap<>();
    private final Map<String, FieldMetaData> exKeyMap=new HashMap<>();

    public Map<String, FieldMetaData> getExValueMap() {
        return exValueMap;
    }

    public void addExValue(String key,FieldMetaData exValue){
        exValueMap.put(key,exValue);
    }

    public Map<String, FieldMetaData> getExKeyMap() {
        return exKeyMap;
    }

    public void addExKey(String key,FieldMetaData exKey){
        exKeyMap.put(key,exKey);
    }
}
