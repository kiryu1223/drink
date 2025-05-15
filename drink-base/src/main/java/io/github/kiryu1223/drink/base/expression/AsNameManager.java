package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.util.DrinkUtil;

import java.util.*;
import java.util.stream.Collectors;

public class AsNameManager {
    private static final ThreadLocal<Map<ISqlTableRefExpression, String>> displayNameMap = new ThreadLocal<>();
    public static void start() {
        displayNameMap.set(new HashMap<>());
    }

    public static void clear() {
        displayNameMap.remove();
    }

    public static Map<ISqlTableRefExpression, String> get() {
        return displayNameMap.get();
    }
}
