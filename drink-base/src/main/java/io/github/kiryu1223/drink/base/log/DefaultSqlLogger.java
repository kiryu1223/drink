package io.github.kiryu1223.drink.base.log;

import io.github.kiryu1223.drink.base.IConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSqlLogger implements ISqlLogger
{
    private static final Logger log = LoggerFactory.getLogger(DefaultSqlLogger.class);

    @Override
    public Logger getLogger()
    {
        return log;
    }
}
