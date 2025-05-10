package io.github.kiryu1223.drink.db.mysql;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDbSupport;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeFactory;
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
    public SqlExpressionFactory getSqlExpressionFactory()
    {
        return new MySQLExpressionFactory();
    }

    @Override
    public IncludeFactory getIncludeFactory()
    {
        return new MySQLIncludeFactory();
    }

    @Override
    public Transformer getTransformer(IConfig config)
    {
        return new MySQLTransformer(config);
    }
}
