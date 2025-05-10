package io.github.kiryu1223.drink.base.toBean.Include;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.session.SqlSession;

import java.util.Collection;
import java.util.List;

public interface IncludeFactory
{
    <T> IncludeBuilder<T> getBuilder(IConfig config, SqlSession session, Class<T> targetClass, Collection<T> sources, List<IncludeSet> includes, ISqlQueryableExpression queryable);
}
