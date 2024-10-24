package io.github.kiryu1223.drink.base.tobean.Include;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.session.SqlSession;

import java.util.Collection;
import java.util.List;

public abstract class IncludeFactory
{
    public abstract <T> IncludeBuilder<T> getBuilder(IConfig config, SqlSession session, Class<T> targetClass, Collection<T> sources, List<IncludeSet> includes, ISqlQueryableExpression queryable);
}
