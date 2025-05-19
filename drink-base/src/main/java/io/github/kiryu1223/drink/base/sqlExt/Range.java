package io.github.kiryu1223.drink.base.sqlExt;

import io.github.kiryu1223.drink.base.exception.Winner;

public final class Range {
    private Range() {
    }

    @SqlExtensionExpression(template = "CURRENT ROW")
    public static Range current() {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "UNBOUNDED PRECEDING")
    public static Range first() {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "{n} PRECEDING")
    public static Range prev(long n) {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "UNBOUNDED FOLLOWING")
    public static Range last() {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "{n} FOLLOWING")
    public static Range next(long n) {
        return Winner.error();
    }
}
