package io.github.kiryu1223.drink.db.h2;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDbSupport;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.transform.Transformer;

public class H2Support implements IDbSupport
{
    @Override
    public DbType getDbType()
    {
        return DbType.H2;
    }

    @Override
    public IDialect getIDialect()
    {
        return new H2Dialect();
    }

    @Override
    public SqlExpressionFactory getSqlExpressionFactory(IConfig config)
    {
        return new H2ExpressionFactory(config);
    }

    @Override
    public Transformer getTransformer(IConfig config)
    {
        return new H2Transformer(config);
    }
}
