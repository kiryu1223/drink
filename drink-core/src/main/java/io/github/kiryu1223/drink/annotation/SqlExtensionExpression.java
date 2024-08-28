package io.github.kiryu1223.drink.annotation;

import io.github.kiryu1223.drink.ext.BaseSqlExtension;
import io.github.kiryu1223.drink.ext.DbType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(SqlExtensionExpressions.class)
public @interface SqlExtensionExpression
{
    DbType dbType() default DbType.Any;
    String function();
    String separator() default ",";
    Class<? extends BaseSqlExtension> extension() default BaseSqlExtension.class;
}
