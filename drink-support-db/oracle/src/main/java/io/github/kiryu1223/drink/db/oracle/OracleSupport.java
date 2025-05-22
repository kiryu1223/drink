package io.github.kiryu1223.drink.db.oracle;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDbSupport;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.transform.ILogic;
import io.github.kiryu1223.drink.base.transform.Transformer;

public class OracleSupport implements IDbSupport
{
    @Override
    public DbType getDbType()
    {
        return DbType.Oracle;
    }

    @Override
    public IDialect getIDialect()
    {
        return new OracleDialect();
    }

    @Override
    public SqlExpressionFactory getSqlExpressionFactory(IConfig config)
    {
        return new OracleExpressionFactory(config);
    }

    @Override
    public Transformer getTransformer(IConfig config)
    {
        return new OracleTransformer(config);
    }
}
