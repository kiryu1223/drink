package io.github.kiryu1223.drink.base;

import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;

public interface IConfig
{
    SqlExpressionFactory getSqlExpressionFactory();

    IDialect getDisambiguation();

    DbType getDbType();
}
