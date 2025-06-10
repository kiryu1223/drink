package io.github.kiryu1223.drink.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Aop {

    public interface OnInsertAction<T> {
        void call(T t);
    }

    private final Map<Class<?>, OnInsertAction<?>> onInsertActionMap = new ConcurrentHashMap<>();

    /**
     * 插入数据时调用
     */
    public <T> void onInsert(Class<T> c, OnInsertAction<T> action) {
        onInsertActionMap.put(c, action);
    }

    public <T> void callOnInsert(T t) {
        if (onInsertActionMap.isEmpty()) return;
        OnInsertAction<T> action = (OnInsertAction<T>) onInsertActionMap.get(t.getClass());
        if (action != null) {
            action.call(t);
        }
    }
}
