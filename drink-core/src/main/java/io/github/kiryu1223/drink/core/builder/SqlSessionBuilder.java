package io.github.kiryu1223.drink.core.builder;

public class SqlSessionBuilder
{
    private SqlSessionBuilder()
    {
    }

    public static SqlSession getSession()
    {
        return new SqlSession();
    }

    public static SqlSession getSession(String key)
    {
        return new SqlSession(key);
    }
}
