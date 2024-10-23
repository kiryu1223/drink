package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.nnnn.expression.SqlQueryableExpression;
import io.github.kiryu1223.drink.core.session.SqlSession;

import java.util.Collection;
import java.util.List;

public abstract class IncludeFactory
{
    public abstract  <T> IncludeBuilder<T> getBuilder(Config config,SqlSession session, Class<T> targetClass, Collection<T> sources, List<IncludeSet> includes, SqlQueryableExpression queryable);
}
