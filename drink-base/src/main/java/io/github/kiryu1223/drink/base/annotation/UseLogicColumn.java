package io.github.kiryu1223.drink.base.annotation;

import io.github.kiryu1223.drink.base.metaData.LogicColumn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UseLogicColumn {
    Class<? extends LogicColumn> value();
}
