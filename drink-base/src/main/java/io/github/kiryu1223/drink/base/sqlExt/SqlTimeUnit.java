package io.github.kiryu1223.drink.base.sqlExt;

import io.github.kiryu1223.drink.base.IConfig;

public enum SqlTimeUnit implements ISqlKeywords
{
    YEAR,
    MONTH,
    WEEK,
    DAY,
    HOUR,
    MINUTE,
    SECOND,
    MILLISECOND,
    MICROSECOND,
    NANOSECOND,
    ;

    @Override
    public String getKeyword(IConfig config)
    {
        return name();
    }
}
