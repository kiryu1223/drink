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
    String value() default "";

    Class<? extends IConverter<?, ?>> converter() default NoConverter.class;
}
