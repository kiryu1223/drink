package io.github.kiryu1223.drink.db.pg;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.session.SqlSession;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeBuilder;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeFactory;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeSet;

import java.util.Collection;
import java.util.List;

public class PostgreSQLIncludeFactory implements IncludeFactory
{
    @Override
    public <T> IncludeBuilder<T> getBuilder(IConfig config, SqlSession session, Class<T> targetClass, Collection<T> sources, List<IncludeSet> includes, ISqlQueryableExpression queryable)
    {
        return new IncludeBuilder<>(config, session, targetClass, sources, includes, queryable);
    }
}
