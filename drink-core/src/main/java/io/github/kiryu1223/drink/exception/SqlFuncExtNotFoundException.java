package io.github.kiryu1223.drink.exception;

import io.github.kiryu1223.drink.base.DbType;

public class SqlFuncExtNotFoundException extends RuntimeException
{
    public SqlFuncExtNotFoundException(DbType type)
    {
        super("No corresponding SqlExtensionExpression annotation found for database type: " + type);
    }
}
