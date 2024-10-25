package io.github.kiryu1223.drink.base.tobean.handler;

import io.github.kiryu1223.drink.base.tobean.handler.impl.datetime.*;
import io.github.kiryu1223.drink.base.tobean.handler.impl.number.*;
import io.github.kiryu1223.drink.base.tobean.handler.impl.other.URLTypeHandler;
import io.github.kiryu1223.drink.base.tobean.handler.impl.other.URLTypeRef;
import io.github.kiryu1223.drink.base.tobean.handler.impl.varchar.CharTypeHandler;
import io.github.kiryu1223.drink.base.tobean.handler.impl.varchar.CharTypeRef;
import io.github.kiryu1223.drink.base.tobean.handler.impl.varchar.StringTypeHandler;
import io.github.kiryu1223.drink.base.tobean.handler.impl.varchar.StringTypeRef;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TypeHandlerManager
{
    private static final Map<Type, ITypeHandler<?>> cache = new HashMap<>();
    private static final UnKnowTypeHandler<?> unKnowTypeHandler = new UnKnowTypeHandler<>();

    public static <T> void set(TypeRef<T> typeRef, ITypeHandler<T> typeHandler)
    {
        cache.put(typeRef.getActualType(), typeHandler);
    }

    public static <T> void set(Class<T> type, ITypeHandler<T> typeHandler)
    {
        cache.put(type, typeHandler);
    }

    static
    {
        //varchar
        set(new CharTypeRef(), new CharTypeHandler());
        set(char.class, new CharTypeHandler());
        set(new StringTypeRef(), new StringTypeHandler());

        //number
        set(new ByteTypeRef(), new ByteTypeHandler());
        set(byte.class, new ByteTypeHandler());
        set(new ShortTypeRef(), new ShortTypeHandler());
        set(short.class, new ShortTypeHandler());
        set(new IntTypeRef(), new IntTypeHandler());
        set(int.class, new IntTypeHandler());
        set(new LongTypeRef(), new LongTypeHandler());
        set(long.class, new LongTypeHandler());
        set(new BoolTypeRef(), new BoolTypeHandler());
        set(boolean.class, new BoolTypeHandler());
        set(new FloatTypeRef(), new FloatTypeHandler());
        set(float.class, new FloatTypeHandler());
        set(new DoubleTypeRef(), new DoubleTypeHandler());
        set(double.class, new DoubleTypeHandler());
        set(new BigIntegerTypeRef(), new BigIntegerTypeHandler());
        set(new BigDecimalTypeRef(), new BigDecimalTypeHandler());

        //datetime
        set(new DateTypeRef(), new DateTypeHandler());
        set(new UtilDateTypeRef(), new UtilDateHandler());
        set(new TimeTypeRef(), new TimeTypeHandler());
        set(new TimestampTypeRef(), new TimestampTypeHandler());
        set(new LocalDateTimeTypeRef(), new LocalDateTimeTypeHandler());
        set(new LocalDateTypeRef(), new LocalDateTypeHandler());
        set(new LocalTimeTypeRef(), new LocalTimeTypeHandler());

        //other
        set(new URLTypeRef(), new URLTypeHandler());
    }

    public static <T> ITypeHandler<T> get(Type type)
    {
        ITypeHandler<T> iTypeHandler = (ITypeHandler<T>) cache.get(type);
        if (iTypeHandler == null)
        {
            return (ITypeHandler<T>) unKnowTypeHandler;
        }
        return iTypeHandler;
    }
}
