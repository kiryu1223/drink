package io.github.kiryu1223.drink.ext;

import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface IConverter<J, D>
{
    D toDb(J value, PropertyMetaData propertyMetaData);

    J toJava(D value, PropertyMetaData propertyMetaData);

    default Class<D> getDbType()
    {
        Type[] interfaces = this.getClass().getGenericInterfaces();
        for (Type type : interfaces)
        {
            if (type instanceof ParameterizedType)
            {
                ParameterizedType pType = (ParameterizedType) type;
                Type dbType = pType.getActualTypeArguments()[1];
                return (Class<D>) dbType;
            }
        }
        throw new RuntimeException();
    }

    default Class<J> getJavaType()
    {
        Type[] interfaces = this.getClass().getGenericInterfaces();
        for (Type type : interfaces)
        {
            if (type instanceof ParameterizedType)
            {
                ParameterizedType pType = (ParameterizedType) type;
                Type javaType = pType.getActualTypeArguments()[0];
                return (Class<J>) javaType;
            }
        }
        throw new RuntimeException();
    }
}
