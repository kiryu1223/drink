package io.github.kiryu1223.drink.base.expression;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExKeys {
    private final ExKey[] exKeys;
    // hashcode key
    private final Map<Integer, List<Object>> valueMap=new HashMap<>();

    public ExKeys(ExKey[] exKeys) {
        this.exKeys = exKeys;
    }

    public ExKey[] getExKeys() {
        return exKeys;
    }

    public void addValue(Integer key, List<Object> values) {
        valueMap.put(key, values);
    }
}

