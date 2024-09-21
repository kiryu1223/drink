package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlQueryableExpression;
import io.github.kiryu1223.drink.core.session.SqlSession;

import java.util.Collection;
import java.util.List;

public abstract class IncludeFactory
{
    protected final Config config;

    protected IncludeFactory(Config config)
    {
        this.config = config;
    }

    public abstract  <T> IncludeBuilder<T> getBuilder(SqlSession session, Class<T> targetClass, Collection<T> sources, List<IncludeSet> includes, SqlQueryableExpression queryable);
}
