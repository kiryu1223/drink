/*
 * Copyright 2017-2024 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.kiryu1223.drink.base.metaData;

import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;

import java.lang.reflect.*;

/**
 * 字段元数据
 *
 * @author kiryu1223
 * @since 3.0
 */
public class FieldMetaData {
    /**
     * 是否不为空
     */
    private final boolean notNull;
    /**
     * 字段名
     */
    private final String fieldName;
    /**
     * 列名
     */
    private final String column;
    /**
     * getter
     */
    private final Method getter;
    /**
     * setter
     */
    private final Method setter;
    /**
     * 字段
     */
    private final Field field;
    /**
     * 是否是泛型类型
     */
    private final boolean isGenericType;
    /**
     * 类型处理器
     */
    private final ITypeHandler<?> typeHandler;
    /**
     * 是否为忽略列
     */
    private final boolean ignoreColumn;
    /**
     * 导航数据
     */
    private final NavigateData navigateData;
    /**
     * 是否为主键
     */
    private final boolean isPrimaryKey;
    /**
     * 是否为自动生成
     */
    private final boolean isGeneratedKey;
    /**
     * 泛型类型
     */
    private final Type genericType;

    public FieldMetaData(boolean notNull, String fieldName, String column, Method getter, Method setter, Field field, ITypeHandler<?> typeHandler, boolean ignoreColumn, NavigateData navigateData, boolean isPrimaryKey, boolean isGeneratedKey) {
        this.notNull = notNull;
        this.fieldName = fieldName;
        this.column = column;
        this.ignoreColumn = ignoreColumn;
        this.isPrimaryKey = isPrimaryKey;
        this.isGeneratedKey = isGeneratedKey;
        getter.setAccessible(true);
        this.getter = getter;
        setter.setAccessible(true);
        this.setter = setter;
        field.setAccessible(true);
        this.field = field;
        this.typeHandler = typeHandler;
        this.navigateData = navigateData;
        this.genericType = field.getGenericType();
        this.isGenericType = genericType instanceof ParameterizedType;
    }

    /**
     * 属性名
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * 列名
     */
    public String getColumn() {
        return column;
    }

    /**
     * getter
     */
    public Method getGetter() {
        return getter;
    }

    /**
     * setter
     */
    public Method getSetter() {
        return setter;
    }

    /**
     * 字段
     */
    public Field getField() {
        return field;
    }

    /**
     * 是否为忽略列
     */
    public boolean isIgnoreColumn() {
        return ignoreColumn;
    }

    /**
     * 是否有导航属性
     */
    public boolean hasNavigate() {
        return navigateData != null;
    }

    /**
     * 导航数据
     */
    public NavigateData getNavigateData() {
        return navigateData;
    }

    /**
     * 父类类型
     */
    public Class<?> getParentType() {
        return field.getDeclaringClass();
    }

    /**
     * 字段类型
     */
    public Class<?> getType() {
        return field.getType();
    }

    /**
     * 字段泛型类型
     */
    public Type getGenericType() {
        return genericType;
    }

    /**
     * 是否为泛型类型
     */
    public boolean isGenericType() {
        return isGenericType;
    }

    /**
     * 是否为主键
     */
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    /**
     * 类型处理器
     */
    public ITypeHandler<?> getTypeHandler() {
        return typeHandler;
    }

    /**
     * 是否不为空
     */
    public boolean isNotNull() {
        return notNull;
    }

    /**
     * 是否为自动生成
     */
    public boolean isGeneratedKey() {
        return isGeneratedKey;
    }

    /**
     * 反射获取值
     */
    public <T> T getValueByObject(Object o) {
        try {
            return (T) getter.invoke(o);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
