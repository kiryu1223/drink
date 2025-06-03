package io.github.kiryu1223.drink.core.util;

import io.github.kiryu1223.drink.base.exception.Winner;
import io.github.kiryu1223.drink.core.api.ITable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class List<T> extends ArrayList<T> {

    public List() {
    }

    public List(Collection<T> collection) {
        super(collection);
    }

    public boolean any() {
        return !isEmpty();
    }

    public boolean any(Function<T, Boolean> func) {
        for (T t : this) {
            if (func.apply(t)) {
                return true;
            }
        }
        return false;
    }

    public long count() {
        return size();
    }

    public long count(Function<T, Boolean> func) {
        long count = 0;
        for (T t : this) {
            if (func.apply(t)) {
                count++;
            }
        }
        return count;
    }

    public <R> R sum(Function<T, R> func) {
        return Winner.error();
    }

    public <R extends Comparable<R>> R max(Function<T, R> func) {
        R max = null;
        for (T t : this) {
            R r = func.apply(t);
            if (max == null || r.compareTo(max) > 0) {
                max = r;
            }
        }
        return max;
    }

    public <R extends Comparable<R>> R min(Function<T, R> func) {
        R min = null;
        for (T t : this) {
            R r = func.apply(t);
            if (min == null || r.compareTo(min) < 0) {
                min = r;
            }
        }
        return min;
    }

    public <R extends Number> R avg(Function<T, R> func) {
        return Winner.error();
    }

    public List<T> distinct() {
        return new List<>(this.stream().distinct().collect(Collectors.toList()));
    }

    public List<T> where(Function<T, Boolean> func) {
        List<T> list = new List<>();
        for (T t : this) {
            if (func.apply(t)) {
                list.add(t);
            }
        }
        return list;
    }

    public <C extends Comparable<C>> List<T> orderBy(Function<T, C> func) {
        List<T> list = new List<>();
        list.addAll(this);
        list.sort(Comparator.comparing(func));
        return list;
    }

    public <C extends Comparable<C>> List<T> orderByDesc(Function<T, C> func) {
        List<T> list = new List<>();
        list.addAll(this);
        list.sort(Comparator.comparing(func).reversed());
        return list;
    }

    public List<T> limit(long offset, long rows) {
        List<T> list = new List<>();
        for (long i = offset; i < offset + rows; i++) {
            if (i >= size()) {
                break;
            }
            list.add(get((int) i));
        }
        return list;
    }

    public <R> List<R> select(Function<T, R> func) {
        List<R> list = new List<>();
        for (T t : this) {
            list.add(func.apply(t));
        }
        return list;
    }

    private T last() {
        return get(size() - 1);
    }

    public T first() {
        return get(0);
    }
}
