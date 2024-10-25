package io.github.kiryu1223.drink.base.tobean.handler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeRef<T>
{
    private final Type actualType;

    public Type getActualType()
    {
        return actualType;
    }

    public TypeRef()
    {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType)
        {
            ParameterizedType superclass = (ParameterizedType) genericSuperclass;
            actualType = superclass.getActualTypeArguments()[0];
        }
        else
        {
            throw new RuntimeException(genericSuperclass.getTypeName());
        }
    }
}
