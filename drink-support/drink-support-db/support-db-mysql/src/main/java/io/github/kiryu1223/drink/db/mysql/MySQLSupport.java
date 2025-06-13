package io.github.kiryu1223.drink.db.mysql;

import io.github.kiryu1223.drink.base.*;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.transform.Transformer;

public class MySQLSupport implements IDbSupport
{
    @Override
    public DbType getDbType()
    {
        return DbType.MySQL;
    }

    @Override
    public IDialect getIDialect()
    {
        return new MySQLDialect();
    }

    @Override
    public SqlExpressionFactory getSqlExpressionFactory(IConfig config)
    {
        return new MySQLExpressionFactory(config);
    }

    @Override
    public Transformer getTransformer(IConfig config)
    {
        return new MySQLTransformer(config);
    }

    @Override
    public IInsertOrUpdate getInsertOrUpdate(IConfig config) {
        return new MySQLInsertOrUpdate(config);
    }
}
