package io.github.kiryu1223.drink.core.log;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.log.ISqlLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSqlLogger implements ISqlLogger {
    private static final Logger log = LoggerFactory.getLogger(DefaultSqlLogger.class);
    private final IConfig config;

    public DefaultSqlLogger(IConfig config) {
        this.config = config;
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public IConfig getConfig() {
        return config;
    }
}
