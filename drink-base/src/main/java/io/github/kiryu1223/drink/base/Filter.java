package io.github.kiryu1223.drink.base;


import io.github.kiryu1223.expressionTree.delegate.Func0;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;
import io.github.kiryu1223.expressionTree.expressions.annos.Expr;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Filter {

    private final Map<Class<?>, Map<String, Pair<Func0<Boolean>, LambdaExpression<?>>>> applyIfMap = new ConcurrentHashMap<>();

    interface ITenant {
        long getTenantId();
    }

    {
        Long tid = new Random().nextLong();

        applyIf(
                ITenant.class,
                "tenant",
                () -> tid > 1000,
                t -> t.getTenantId() == tid
        );
    }

    public <T> void apply(Class<T> t, String filterId, @Expr(Expr.BodyType.Expr) Func1<T, Boolean> func) {

    }

    public <T> void apply(Class<T> t, String filterId, ExprTree<Func1<T, Boolean>> func) {
        Map<String, Pair<Func0<Boolean>, LambdaExpression<?>>> stringPairMap = applyIfMap.computeIfAbsent(t, k -> new HashMap<>());
        stringPairMap.put(filterId, new Pair<>(null, func.getTree()));
    }

    public <T> void applyIf(Class<T> t, String filterId, Func0<Boolean> cond, @Expr(Expr.BodyType.Expr) Func1<T, Boolean> func) {

    }

    public <T> void applyIf(Class<T> t, String filterId, Func0<Boolean> cond, ExprTree<Func1<T, Boolean>> func) {
        Map<String, Pair<Func0<Boolean>, LambdaExpression<?>>> stringPairMap = applyIfMap.computeIfAbsent(t, k -> new HashMap<>());
        stringPairMap.put(filterId, new Pair<>(cond, func.getTree()));
    }

    public <T> List<LambdaExpression<?>> getApplyList(Class<T> targetType, List<String> ignoreFilterIds) {
        Map<String, Pair<Func0<Boolean>, LambdaExpression<?>>> stringPairMap = applyIfMap.get(targetType);
        if (stringPairMap != null) {
            return stringPairMap.entrySet().stream()
                    .filter(e -> ignoreFilterIds.isEmpty() || !ignoreFilterIds.contains(e.getKey()))
                    .map(e -> e.getValue())
                    .filter(p -> p.getKey().invoke())
                    .map(p -> p.getValue())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
