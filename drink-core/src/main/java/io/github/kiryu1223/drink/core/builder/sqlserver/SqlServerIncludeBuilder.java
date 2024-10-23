package io.github.kiryu1223.drink.core.builder.sqlserver;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.builder.IncludeBuilder;
import io.github.kiryu1223.drink.core.builder.IncludeSet;
import io.github.kiryu1223.drink.nnnn.expression.SqlQueryableExpression;
import io.github.kiryu1223.drink.core.session.SqlSession;

import java.util.Collection;
import java.util.List;

public class SqlServerIncludeBuilder<T> extends IncludeBuilder<T>
{
    public SqlServerIncludeBuilder(Config config, SqlSession session, Class<T> targetClass, Collection<T> sources, List<IncludeSet> includes, SqlQueryableExpression queryable)
    {
        super(config, session, targetClass, sources, includes, queryable);
    }

    @Override
    protected void rowNumber(List<String> rowNumberFunction, List<SqlExpression> rowNumberParams)
    {
        rowNumberFunction.add("ROW_NUMBER() OVER (PARTITION BY ");
        rowNumberFunction.add(" ORDER BY ");
        if (rowNumberParams.size() <= 1)
        {
            rowNumberParams.add(rowNumberParams.get(0));
        }
        for (int i = 0; i < rowNumberParams.size(); i++)
        {
            if (i < rowNumberParams.size() - 2) rowNumberFunction.add(",");
        }
        rowNumberFunction.add(")");
    }
}
