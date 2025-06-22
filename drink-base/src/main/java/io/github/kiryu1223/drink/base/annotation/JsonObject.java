package io.github.kiryu1223.drink.base.annotation;

import io.github.kiryu1223.drink.base.toBean.handler.JsonTypeHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记为数据库json对象
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE})
public @interface JsonObject {}
