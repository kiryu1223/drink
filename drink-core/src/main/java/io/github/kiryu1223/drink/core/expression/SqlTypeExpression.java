package io.github.kiryu1223.drink.core.expression;

public abstract class SqlTypeExpression extends SqlExpression
{
    protected final Class<?> type;

    protected SqlTypeExpression(Class<?> type)
    {
        this.type = type;
    }

    protected boolean isBool()
    {
        return type == boolean.class || type == Boolean.class;
    }

    protected boolean isStringOrChar()
    {
        return type == String.class || type == char.class || type == Character.class;
    }
}
