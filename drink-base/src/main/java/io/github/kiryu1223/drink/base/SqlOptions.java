package io.github.kiryu1223.drink.base;

import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ServiceLoader;

public class SqlOptions {
    private static final ThreadLocal<SqlOption> option = new ThreadLocal<>();
    private static final ThreadLocal<Deque<ISqlQueryableExpression>> currentQueryable = new ThreadLocal<>();

    static {
        currentQueryable.set(new ArrayDeque<>());
    }

    public static Deque<ISqlQueryableExpression> getCurrentQueryable() {
        return currentQueryable.get();
    }

    public static SqlOption getOption() {
        return option.get();
    }

    public static void setOption(SqlOption sqlOption) {
        option.set(sqlOption);
    }

    public static void clear() {
        option.remove();
    }
}
