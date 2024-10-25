package io.github.kiryu1223.drink.core.include.sqlserver;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeBuilder;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeSet;
import io.github.kiryu1223.drink.base.session.SqlSession;

import java.util.Collection;
import java.util.List;

public class SqlServerIncludeBuilder<T> extends IncludeBuilder<T>
{
    public SqlServerIncludeBuilder(IConfig config, SqlSession session, Class<T> targetClass, Collection<T> sources, List<IncludeSet> includes, ISqlQueryableExpression queryable)
    {
        super(config, session, targetClass, sources, includes, queryable);
    }

    @Override
    protected void rowNumber(List<String> rowNumberFunction, List<ISqlExpression> rowNumberParams)
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
