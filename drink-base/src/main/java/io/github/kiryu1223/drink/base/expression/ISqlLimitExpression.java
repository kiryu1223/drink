package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

public interface ISqlLimitExpression extends ISqlExpression
{
    long getOffset();

    long getRows();

    void setOffset(long offset);

    void setRows(long rows);

    default boolean onlyHasRows()
    {
        return getRows() > 0 && getOffset() <= 0;
    }

    default boolean hasRowsAndOffset()
    {
        return getRows() > 0 && getOffset() > 0;
    }

    default boolean hasRowsOrOffset()
    {
        return getRows() > 0 || getOffset() > 0;
    }

    @Override
    default ISqlLimitExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.limit(getOffset(), getRows());
    }
}
