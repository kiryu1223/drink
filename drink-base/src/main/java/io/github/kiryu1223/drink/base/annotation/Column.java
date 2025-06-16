package io.github.kiryu1223.drink.base.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column
{
    /**
     * 是否为主键
     */
    boolean primaryKey() default false;

    /**
     * 数据库列名
     */
    String value() default "";

    /**
     * 是否是数据库生成值(自增or默认值等)
     */
    boolean generatedKey() default false;

    /**
     * 是否不为空
     */
    boolean notNull() default false;
}
