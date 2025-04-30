package io.github.kiryu1223.drink.base.annotation;


import io.github.kiryu1223.drink.base.metaData.IMappingTable;

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
     * 目标类型(仅少数场合需要手动填写)
     */
    Class<?> targetType() default Empty.class;

    /**
     * 自身对应java字段名
     */
    String self();

    /**
     * 目标对应java字段名
     */
    String target();

    /**
     * 中间表，需要继承{@link IMappingTable}
     */
    Class<? extends IMappingTable> mappingTable() default IMappingTable.class;

    /**
     * self对应的mapping表java字段名
     */
    String selfMapping() default "";

    /**
     * target对应的mapping表java字段名
     */
    String targetMapping() default "";
}
