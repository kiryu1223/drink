package io.github.kiryu1223.drink.base;

import io.github.kiryu1223.drink.base.sqlExt.SqlExtensionExpression;
import io.github.kiryu1223.expressionTree.delegate.Action0;
import io.github.kiryu1223.expressionTree.delegate.Action1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Aop {

    private final Map<Class<?>, Action1<?>> onInsertActionMap = new ConcurrentHashMap<>();
    private final Map<Class<?>, Action1<?>> onUpdateActionMap = new ConcurrentHashMap<>();

    /**
     * 插入数据时调用
     */
    public <T> void onInsert(Class<T> c, Action1<T> action) {
        onInsertActionMap.put(c, action);
    }

    public <T> void callOnInsert(T t) {
        if (onInsertActionMap.isEmpty()) return;
        Action1<T> action = (Action1<T>) onInsertActionMap.get(t.getClass());
        if (action != null) {
            action.invoke(t);
        }
    }

    public <T> void onUpdate(Class<T> c, Action1<T> action) {
        onUpdateActionMap.put(c, action);
    }

    public <T> void callOnUpdate(T t) {
        if (onUpdateActionMap.isEmpty()) return;
        Action1<T> action = (Action1<T>) onUpdateActionMap.get(t.getClass());
        if (action != null) {
            action.invoke(t);
        }
    }
}
