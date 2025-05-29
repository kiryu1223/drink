package io.github.kiryu1223.drink.base.expression;

import java.util.*;

public class ExKeys {
    private final ExKey[] exKeys;
    // hashcode key
    private final Map<Integer, List<Object>> valueMap=new HashMap<>();

    public ExKeys(List<ExKey> exKeys) {
        this.exKeys = exKeys.toArray(new ExKey[0]);
    }

    public ExKey[] getExKeys() {
        return exKeys;
    }

    public void addValue(Integer key, Object value) {
        List<Object> objects = valueMap.computeIfAbsent(key, k -> new ArrayList<>());
        objects.add(value);
    }
}

