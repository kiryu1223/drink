package io.github.kiryu1223.drink.core.api.crud.read.pivot;

import java.util.Arrays;
import java.util.List;

public class TransPair<T> {
    private final T t;
    private final String asName;

    public TransPair(T t, String asName) {
        this.t = t;
        this.asName = asName;
    }

    public T getT() {
        return t;
    }

    public String getAsName() {
        return asName;
    }

    public static <T> TransPair<T> of(T t, String as) {
        return new TransPair<>(t, as);
    }

    public static <T> List<TransPair<T>> of(T t1, String as1, T t2, String as2) {
        return Arrays.asList(of(t1, as1), of(t2, as2));
    }

    public static <T> List<TransPair<T>> of(T t1, String as1, T t2, String as2, T t3, String as3) {
        return Arrays.asList(of(t1, as1), of(t2, as2), of(t3, as3));
    }

    public static <T> List<TransPair<T>> of(T t1, String as1, T t2, String as2, T t3, String as3, T t4, String as4) {
        return Arrays.asList(of(t1, as1), of(t2, as2), of(t3, as3), of(t4, as4));
    }

    public static <T> List<TransPair<T>> of(T t1, String as1, T t2, String as2, T t3, String as3, T t4, String as4, T t5, String as5) {
        return Arrays.asList(of(t1, as1), of(t2, as2), of(t3, as3), of(t4, as4), of(t5, as5));
    }

    public static <T> List<TransPair<T>> of(T t1, String as1, T t2, String as2, T t3, String as3, T t4, String as4, T t5, String as5, T t6, String as6) {
        return Arrays.asList(of(t1, as1), of(t2, as2), of(t3, as3), of(t4, as4), of(t5, as5), of(t6, as6));
    }

    public static <T> List<TransPair<T>> of(T t1, String as1, T t2, String as2, T t3, String as3, T t4, String as4, T t5, String as5, T t6, String as6, T t7, String as7) {
        return Arrays.asList(of(t1, as1), of(t2, as2), of(t3, as3), of(t4, as4), of(t5, as5), of(t6, as6), of(t7, as7));
    }

    public static <T> List<TransPair<T>> of(T t1, String as1, T t2, String as2, T t3, String as3, T t4, String as4, T t5, String as5, T t6, String as6, T t7, String as7, T t8, String as8) {
        return Arrays.asList(of(t1, as1), of(t2, as2), of(t3, as3), of(t4, as4), of(t5, as5), of(t6, as6), of(t7, as7), of(t8, as8));
    }

    public static <T> List<TransPair<T>> of(T t1, String as1, T t2, String as2, T t3, String as3, T t4, String as4, T t5, String as5, T t6, String as6, T t7, String as7, T t8, String as8, T t9, String as9) {
        return Arrays.asList(of(t1, as1), of(t2, as2), of(t3, as3), of(t4, as4), of(t5, as5), of(t6, as6), of(t7, as7), of(t8, as8), of(t9, as9));
    }

    public static <T> List<TransPair<T>> of(T t1, String as1, T t2, String as2, T t3, String as3, T t4, String as4, T t5, String as5, T t6, String as6, T t7, String as7, T t8, String as8, T t9, String as9, T t10, String as10) {
        return Arrays.asList(of(t1, as1), of(t2, as2),  of(t3, as3), of(t4, as4), of(t5, as5), of(t6, as6), of(t7, as7), of(t8, as8), of(t9, as9), of(t10, as10));
    }
}
