package io.github.kiryu1223.drink.base.sqlExt;

import io.github.kiryu1223.drink.base.DbType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(SqlExtensionExpressions.class)
public @interface SqlExtensionExpression
{
    DbType dbType() default DbType.Any;

    String template();

    String separator() default ",";

    Class<? extends BaseSqlExtension> extension() default BaseSqlExtension.class;
}
