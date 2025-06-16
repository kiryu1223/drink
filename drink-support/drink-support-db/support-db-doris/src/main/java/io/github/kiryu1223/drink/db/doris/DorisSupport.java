package io.github.kiryu1223.drink.db.doris;

import io.github.kiryu1223.drink.base.*;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.transform.Transformer;

public class DorisSupport implements IDbSupport
{
    @Override
    public DbType getDbType()
    {
        return DbType.Doris;
    }

    @Override
    public IDialect getIDialect()
    {
        return new DorisDialect();
    }

    @Override
    public SqlExpressionFactory getSqlExpressionFactory(IConfig config)
    {
        return new DorisExpressionFactory(config);
    }

    @Override
    public Transformer getTransformer(IConfig config)
    {
        return new DorisTransformer(config);
    }

    @Override
    public IInsertOrUpdate getInsertOrUpdate(IConfig config) {
        return new DorisInsertOrUpdate(config);
    }
}
