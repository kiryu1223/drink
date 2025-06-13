package io.github.kiryu1223.drink.db.sqlite;

import io.github.kiryu1223.drink.base.*;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.transform.Transformer;

public class SQLiteSupport implements IDbSupport
{
    @Override
    public DbType getDbType()
    {
        return DbType.SQLite;
    }

    @Override
    public IDialect getIDialect()
    {
        return new SQLiteDialect();
    }

    @Override
    public SqlExpressionFactory getSqlExpressionFactory(IConfig config)
    {
        return new SQLiteExpressionFactory(config);
    }

    @Override
    public Transformer getTransformer(IConfig config)
    {
        return null;
    }

    @Override
    public IInsertOrUpdate getInsertOrUpdate(IConfig config)
    {
        return new SQLiteInsertOrUpdate();
    }
}
