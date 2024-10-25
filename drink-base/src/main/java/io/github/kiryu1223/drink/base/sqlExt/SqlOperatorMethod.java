package io.github.kiryu1223.drink.base.sqlExt;


import io.github.kiryu1223.drink.base.expression.SqlOperator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SqlOperatorMethod
{
    SqlOperator value();
}
