package io.github.kiryu1223.drink.exception;

public class SqlFuncExtNotFoundException extends RuntimeException
{
    public SqlFuncExtNotFoundException(DbType type)
    {
        super("No corresponding SqlExtensionExpression annotation found for database type: " + type);
    }
}
