package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.expression.ISqlWithExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

public class SqlWithExpression implements ISqlWithExpression {

    protected final ISqlQueryableExpression queryable;
    protected final String name;

    protected SqlWithExpression(ISqlQueryableExpression queryable, String name) {
        this.queryable = queryable;
        this.name = name;
    }

    @Override
    public ISqlQueryableExpression getQueryable() {
        return queryable;
    }

    @Override
    public String withTableName() {
        return name;
    }

    public String getWith(IConfig config, List<SqlValue> values)
    {
        IDialect disambiguation = config.getDisambiguation();
        return disambiguation.disambiguationTableName(name) + " AS (" + queryable.getSqlAndValue(config, values) + ")";
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        IDialect disambiguation = config.getDisambiguation();
        return disambiguation.disambiguationTableName(name);
    }
}
