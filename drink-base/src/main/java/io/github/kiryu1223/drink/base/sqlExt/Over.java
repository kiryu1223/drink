package io.github.kiryu1223.drink.base.sqlExt;

import io.github.kiryu1223.drink.base.exception.Winner;

import java.math.BigDecimal;

public final class Over
{
    private Over() {}

    // region [参数]

    public static final class Param
    {
        private Param()
        {
        }
    }

    @SqlExtensionExpression(template = "PARTITION BY {partition}")
    public static <P> Param partitionBy(P partition)
    {
        return Winner.error();
    }

    @SafeVarargs
    @SqlExtensionExpression(template = "PARTITION BY {partition},{ps}")
    public static <P> Param partitionBy(P partition, P... ps)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "ORDER BY {orderBy}")
    public static <O> Param orderBy(O orderBy)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "ROWS BETWEEN {start} AND {end}")
    public static Param between(Rows start, Rows end)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "RANGE BETWEEN {start} AND {end}")
    public static Param between(Range start, Range end)
    {
        return Winner.error();
    }

    // endregion

    // region [返回值]

    @SqlExtensionExpression(template = "COUNT(*) {super}")
    public long count()
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "COUNT({r}) {super}")
    public <R> long count(R r)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "SUM({r}) {super}")
    public <R> R sum(R r)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "AVG({r}) {super}")
    public BigDecimal avg(Number r)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "MAX({r}) {super}")
    public <R> R max(R r)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "MIN({r}) {super}")
    public <R> R min(R r)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "ROW_NUMBER() {super}")
    public long rowNumber()
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "RANK() {super}")
    public long rank()
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "DENSE_RANK() {super}")
    public long denseRank()
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "PERCENT_RANK() {super}")
    public double percentRank()
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "NTILE({n}) {super}")
    public long ntile(long n)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "CUME_DIST({n}) {super}")
    public double cumeDist()
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "LAG({expr}) {super}")
    public <R> R lag(R expr)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "LAG({expr},{offset}) {super}")
    public <R> R lag(R expr, long offset)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "LAG({expr},{offset},{publicValue}) {super}")
    public <R> R lag(R expr, long offset, R publicValue)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "LEAD({expr}) {super}")
    public <R> R lead(R expr)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "LEAD({expr},{offset}) {super}")
    public <R> R lead(R expr, long offset)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "LEAD({expr},{offset},{publicValue}) {super}")
    public <R> R lead(R expr, long offset, R publicValue)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "FIRST_VALUE({expr}) {super}")
    public <R> R firstValue(R expr)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "LAST_VALUE({expr}) {super}")
    public <R> R lastValue(R expr)
    {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "NTH_VALUE({expr},{n}) {super}")
    public <R> R nthValue(R expr, long n)
    {
        return Winner.error();
    }

    // endregion
}

