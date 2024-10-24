package io.github.kiryu1223.drink.include.sqlserver;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.session.SqlSession;
import io.github.kiryu1223.drink.base.tobean.Include.IncludeBuilder;
import io.github.kiryu1223.drink.base.tobean.Include.IncludeFactory;
import io.github.kiryu1223.drink.base.tobean.Include.IncludeSet;

import java.util.Collection;
import java.util.List;

public class SqlServerIncludeFactory extends IncludeFactory
{
    @Override
    public <T> IncludeBuilder<T> getBuilder(IConfig config, SqlSession session, Class<T> targetClass, Collection<T> sources, List<IncludeSet> includes, ISqlQueryableExpression queryable)
    {
        return new SqlServerIncludeBuilder<>(config, session, targetClass, sources, includes, queryable);
    }
}
