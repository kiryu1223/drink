package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.config.Config;

public class SqlSessionBuilder
{
    private SqlSessionBuilder()
    {
    }

    public static SqlSession getSession(Config config)
    {
        return new SqlSession(config);
    }
}
