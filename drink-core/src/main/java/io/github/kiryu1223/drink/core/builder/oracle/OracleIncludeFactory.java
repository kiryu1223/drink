package io.github.kiryu1223.drink.core.builder.oracle;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.builder.IncludeBuilder;
import io.github.kiryu1223.drink.core.builder.IncludeFactory;
import io.github.kiryu1223.drink.core.builder.IncludeSet;
import io.github.kiryu1223.drink.core.expression.SqlQueryableExpression;
import io.github.kiryu1223.drink.core.session.SqlSession;

import java.util.Collection;
import java.util.List;

public class OracleIncludeFactory extends IncludeFactory
{
    public OracleIncludeFactory(Config config)
    {
        super(config);
    }

    @Override
    public <T> IncludeBuilder<T> getBuilder(SqlSession session, Class<T> targetClass, Collection<T> sources, List<IncludeSet> includes, SqlQueryableExpression queryable)
    {
        return new IncludeBuilder<>(config, session, targetClass, sources, includes, queryable);
    }
}
