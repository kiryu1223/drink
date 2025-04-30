package io.github.kiryu1223.drink.base.session;

import io.github.kiryu1223.drink.base.IConfig;

public interface SqlSessionFactory
{
    SqlSession getSession(IConfig config);
}
