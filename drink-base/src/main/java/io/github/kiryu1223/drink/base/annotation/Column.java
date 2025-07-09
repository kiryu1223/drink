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
     * 是否是自增键（非插入默认值）
     */
    boolean generatedKey() default false;

    /**
     * 是否不为空
     */
    boolean notNull() default false;
}
