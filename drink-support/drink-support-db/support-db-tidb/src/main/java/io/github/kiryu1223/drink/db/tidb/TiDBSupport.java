package io.github.kiryu1223.drink.db.tidb;

import io.github.kiryu1223.drink.base.*;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.transform.Transformer;

public class TiDBSupport implements IDbSupport {
    @Override
    public DbType getDbType() {
        return DbType.TiDB;
    }

    @Override
    public IDialect getIDialect() {
        return new TiDBDialect();
    }

    @Override
    public SqlExpressionFactory getSqlExpressionFactory(IConfig config) {
        return new TiDBExpressionFactory(config);
    }

    @Override
    public Transformer getTransformer(IConfig config) {
        return new TiDBTransformer(config);
    }

    @Override
    public IInsertOrUpdate getInsertOrUpdate(IConfig config) {
        return new TiDBInsertOrUpdate(config);
    }
}
