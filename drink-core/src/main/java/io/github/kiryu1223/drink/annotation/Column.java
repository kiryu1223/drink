package io.github.kiryu1223.drink.annotation;

import io.github.kiryu1223.drink.ext.IConverter;
import io.github.kiryu1223.drink.ext.NoConverter;

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
     * 转换器，用于列类型与java类型不一致的情况（比如数据库枚举=>java枚举）
     */
    Class<? extends IConverter<?, ?>> converter() default NoConverter.class;
}
