package io.github.kiryu1223.drink.db.sqlserver;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDbSupport;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.transform.ILogic;
import io.github.kiryu1223.drink.base.transform.Transformer;

public class SQLServerSupport implements IDbSupport
{
    @Override
    public DbType getDbType()
    {
        return DbType.SQLServer;
    }

    @Override
    public IDialect getIDialect()
    {
        return new SQLServerDialect();
    }

    @Override
    public SqlExpressionFactory getSqlExpressionFactory(IConfig config)
    {
        return new SQLServerExpressionFactory(config);
    }

    @Override
    public Transformer getTransformer(IConfig config)
    {
        return new SQLServerTransformer(config);
    }
}
