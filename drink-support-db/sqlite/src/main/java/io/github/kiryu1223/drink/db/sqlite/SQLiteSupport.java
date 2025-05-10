package io.github.kiryu1223.drink.db.sqlite;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDbSupport;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeFactory;
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
    public SqlExpressionFactory getSqlExpressionFactory()
    {
        return new SqliteExpressionFactory();
    }

    @Override
    public IncludeFactory getIncludeFactory()
    {
        return new SQLiteIncludeFactory();
    }

    @Override
    public Transformer getTransformer(IConfig config)
    {
        return null;
    }
}
