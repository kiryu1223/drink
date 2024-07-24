package io.github.kiryu1223.drink.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Navigate
{
    /**
     * 关联关系
     */
    RelationType value();
    /**
     * 自身对应java字段名
     */
    String self();
    /**
     * 目标对应java字段名
     */
    String target();
}
