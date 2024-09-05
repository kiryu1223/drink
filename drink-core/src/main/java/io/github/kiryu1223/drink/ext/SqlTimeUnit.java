package io.github.kiryu1223.drink.ext;

import io.github.kiryu1223.drink.config.Config;

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
    public String getKeyword(Config config)
    {
        return name();
    }
}
