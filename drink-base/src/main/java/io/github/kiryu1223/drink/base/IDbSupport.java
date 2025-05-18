package io.github.kiryu1223.drink.base;

import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeFactory;
import io.github.kiryu1223.drink.base.transform.Transformer;

public interface IDbSupport
{
    DbType getDbType();
    IDialect getIDialect();
    SqlExpressionFactory getSqlExpressionFactory(IConfig config);
    IncludeFactory getIncludeFactory();
    Transformer getTransformer(IConfig config);
}
