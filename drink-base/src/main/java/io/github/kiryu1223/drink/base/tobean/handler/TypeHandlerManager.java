package io.github.kiryu1223.drink.base.tobean.handler;

import io.github.kiryu1223.drink.base.tobean.handler.impl.datetime.*;
import io.github.kiryu1223.drink.base.tobean.handler.impl.number.*;
import io.github.kiryu1223.drink.base.tobean.handler.impl.other.URLTypeHandler;
import io.github.kiryu1223.drink.base.tobean.handler.impl.varchar.CharTypeHandler;
import io.github.kiryu1223.drink.base.tobean.handler.impl.varchar.StringTypeHandler;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        set(new TypeRef<Character>(){}, new CharTypeHandler());
        set(char.class, new CharTypeHandler());
        set(new TypeRef<String>(){}, new StringTypeHandler());

        //number
        set(new TypeRef<Byte>(){}, new ByteTypeHandler());
        set(byte.class, new ByteTypeHandler());
        set(new TypeRef<Short>(){}, new ShortTypeHandler());
        set(short.class, new ShortTypeHandler());
        set(new TypeRef<Integer>(){}, new IntTypeHandler());
        set(int.class, new IntTypeHandler());
        set(new TypeRef<Long>(){}, new LongTypeHandler());
        set(long.class, new LongTypeHandler());
        set(new TypeRef<Boolean>(){}, new BoolTypeHandler());
        set(boolean.class, new BoolTypeHandler());
        set(new TypeRef<Float>(){}, new FloatTypeHandler());
        set(float.class, new FloatTypeHandler());
        set(new TypeRef<Double>(){}, new DoubleTypeHandler());
        set(double.class, new DoubleTypeHandler());
        set(new TypeRef<BigInteger>(){}, new BigIntegerTypeHandler());
        set(new TypeRef<BigDecimal>(){}, new BigDecimalTypeHandler());

        //datetime
        set(new TypeRef<Date>(){}, new DateTypeHandler());
        set(new TypeRef<java.util.Date>(){}, new UtilDateHandler());
        set(new TypeRef<Time>(){}, new TimeTypeHandler());
        set(new TypeRef<Timestamp>(){}, new TimestampTypeHandler());
        set(new TypeRef<LocalDateTime>(){}, new LocalDateTimeTypeHandler());
        set(new TypeRef<LocalDate>(){}, new LocalDateTypeHandler());
        set(new TypeRef<LocalTime>(){}, new LocalTimeTypeHandler());

        //other
        set(new TypeRef<URL>(){}, new URLTypeHandler());
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
