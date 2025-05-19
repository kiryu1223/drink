package io.github.kiryu1223.drink.base.sqlExt;

import io.github.kiryu1223.drink.base.exception.Winner;

public final class Rows {
    private Rows() {
    }

    @SqlExtensionExpression(template = "CURRENT ROW")
    public static Rows current() {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "UNBOUNDED PRECEDING")
    public static Rows first() {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "{n} PRECEDING")
    public static Rows prev(long n) {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "UNBOUNDED FOLLOWING")
    public static Rows last() {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "{n} FOLLOWING")
    public static Rows next(long n) {
        return Winner.error();
    }
}
