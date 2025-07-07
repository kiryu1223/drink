package io.github.kiryu1223.drink.base;

import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.transform.Transformer;

public interface IDbSupport {
    DbType getDbType();
    IDialect getIDialect();
    SqlExpressionFactory getSqlExpressionFactory(IConfig config);
    Transformer getTransformer(IConfig config);
    IInsertOrUpdate getInsertOrUpdate(IConfig config);
}
