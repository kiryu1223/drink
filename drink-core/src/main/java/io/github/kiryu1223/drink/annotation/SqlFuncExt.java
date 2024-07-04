package io.github.kiryu1223.drink.annotation;

import io.github.kiryu1223.drink.ext.DbType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(SqlFuncExts.class)
public @interface SqlFuncExt
{
    DbType dbType() default DbType.Other;
    String function();
}
