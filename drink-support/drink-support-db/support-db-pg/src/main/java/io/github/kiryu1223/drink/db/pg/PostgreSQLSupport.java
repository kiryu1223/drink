package io.github.kiryu1223.drink.db.pg;

import io.github.kiryu1223.drink.base.*;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.transform.Transformer;

public class PostgreSQLSupport implements IDbSupport {
    @Override
    public DbType getDbType() {
        return DbType.PostgreSQL;
    }

    @Override
    public IDialect getIDialect() {
        return new PostgreSQLDialect();
    }

    @Override
    public SqlExpressionFactory getSqlExpressionFactory(IConfig config) {
        return new PostgreSQLExpressionFactory(config);
    }

    @Override
    public Transformer getTransformer(IConfig config) {
        return new PostgreSQLTransformer(config);
    }

    @Override
    public IInsertOrUpdate getInsertOrUpdate(IConfig config) {
        return new PostgreInsertOrUpdate(config);
    }
}
