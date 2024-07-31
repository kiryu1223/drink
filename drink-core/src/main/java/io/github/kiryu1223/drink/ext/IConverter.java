package io.github.kiryu1223.drink.ext;

import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.core.visitor.ExpressionUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface IConverter<J, D>
{
    D toDb(J value, PropertyMetaData propertyMetaData);

    J toJava(D value, PropertyMetaData propertyMetaData);

    default Class<D> getDbType()
    {
        Type[] interfaces = this.getClass().getGenericInterfaces();
        Type type = interfaces[0];
        return ExpressionUtil.getType(type, 1);
    }

    default Class<J> getJavaType()
    {
        Type[] interfaces = this.getClass().getGenericInterfaces();
        Type type = interfaces[0];
        return ExpressionUtil.getType(type, 0);
    }
}
