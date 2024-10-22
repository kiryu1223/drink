package io.github.kiryu1223.drink.extensions.expression;


public interface ISqlTableExpression extends ISqlExpression
{
    Class<?> getTableClass();
}
