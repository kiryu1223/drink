package io.github.kiryu1223.drink.base.metaData;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.cast;

public class LogicColumn {
    public LogicColumn() {

    }
    public ISqlExpression onRead(IConfig config, ISqlColumnExpression column) {
        return column;
    }

    public String onWrite(IConfig config) {
        return "?";
    }

    private static final Map<Class<? extends LogicColumn>, ? extends LogicColumn> map = new ConcurrentHashMap<>();

    public static LogicColumn get(Class<? extends LogicColumn> clazz) {
        return map.computeIfAbsent(clazz, k -> {
            try {
                return cast(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DrinkException(e);
            }
        });
    }
}
