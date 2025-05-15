package io.github.kiryu1223.drink.db.mysql;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.expression.impl.SqlRecursionExpression;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;

public class MySQLRecursionExpression extends SqlRecursionExpression {

    public MySQLRecursionExpression(ISqlQueryableExpression queryable, FieldMetaData parentField, FieldMetaData childField, int level) {
        super(queryable, parentField, childField, level);
    }

    @Override
    protected String getStart(IConfig config) {
        IDialect dialect = config.getDisambiguation();
        return "RECURSIVE " + dialect.disambiguationTableName(withTableName()) + " AS";
    }
}
