package io.github.kiryu1223.drink.base.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExValues {
    // key = value:xx
    private final Map<String,ExValue> exValueMap=new HashMap<>();

    public Map<String, ExValue> getExValueMap() {
        return exValueMap;
    }

    public void addExValue(String key,ExValue exValue){
        exValueMap.put(key,exValue);
    }

    public void clear() {
        exValueMap.clear();
    }
}
