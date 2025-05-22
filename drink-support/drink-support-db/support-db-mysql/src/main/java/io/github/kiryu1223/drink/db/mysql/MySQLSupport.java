package io.github.kiryu1223.drink.db.mysql;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDbSupport;
import io.github.kiryu1223.drink.base.IDialect;
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
}
