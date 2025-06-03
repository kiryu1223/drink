package io.github.kiryu1223.drink.base.toBean.handler;

import io.github.kiryu1223.drink.base.toBean.handler.impl.datetime.*;
import io.github.kiryu1223.drink.base.toBean.handler.impl.list.*;
import io.github.kiryu1223.drink.base.toBean.handler.impl.number.*;
import io.github.kiryu1223.drink.base.toBean.handler.impl.other.URLTypeHandler;
import io.github.kiryu1223.drink.base.toBean.handler.impl.varchar.CharTypeHandler;
import io.github.kiryu1223.drink.base.toBean.handler.impl.varchar.StringTypeHandler;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TypeHandlerManager
{
    private static final Map<Type, ITypeHandler<?>> cache = new HashMap<>();
    private static final Map<Class<? extends ITypeHandler<?>>, ITypeHandler<?>> handlerCache = new HashMap<>();
    private static final UnKnowTypeHandler<?> unKnowTypeHandler = new UnKnowTypeHandler<>();

//    public static <T> void set(TypeRef<T> typeRef, ITypeHandler<T> typeHandler)
//    {
//        cache.put(typeRef.getActualType(), typeHandler);
//    }

    public static UnKnowTypeHandler<?> getUnKnowTypeHandler()
    {
        return unKnowTypeHandler;
    }

    public static <T> void set(ITypeHandler<T> typeHandler)
    {
        Type actualType = typeHandler.getActualType();
        warpBaseType(actualType, typeHandler);
        cache.put(actualType, typeHandler);
    }

    static
    {
        //varchar
        set(new CharTypeHandler());
        set(new StringTypeHandler());

        //number
        set(new ByteTypeHandler());
        set(new ShortTypeHandler());
        set(new IntTypeHandler());
        set(new LongTypeHandler());
        set(new BoolTypeHandler());
        set(new FloatTypeHandler());
        set(new DoubleTypeHandler());
        set(new BigIntegerTypeHandler());
        set(new BigDecimalTypeHandler());

        //datetime
        set(new DateTypeHandler());
        set(new UtilDateHandler());
        set(new TimeTypeHandler());
        set(new TimestampTypeHandler());
        set(new LocalDateTimeTypeHandler());
        set(new LocalDateTypeHandler());
        set(new LocalTimeTypeHandler());

        //other
        set(new URLTypeHandler());

        // list
        set(new ListStringHandler());
        set(new ListIntHandler());
        set(new ListLongHandler());
        set(new ListBoolHandler());
        set(new ListFloatHandler());
        set(new ListDoubleHandler());
        set(new ListCharHandler());
        set(new ListByteHandler());
        set(new ListShortHandler());
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

    /**
     * 通过处理器类型获取处理器
     *
     * @param handlerType 处理器类型
     */
    public static <T> ITypeHandler<T> getByHandlerType(Class<? extends ITypeHandler<T>> handlerType) {
        ITypeHandler<T> typeHandler = (ITypeHandler<T>) handlerCache.get(handlerType);
        if (typeHandler == null) {
            try {
                typeHandler = handlerType.newInstance();
                handlerCache.put(handlerType, typeHandler);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return typeHandler;
    }

    private static void warpBaseType(Type actualType, ITypeHandler<?> typeHandler)
    {
        if (actualType == Character.class)
        {
            cache.put(char.class, typeHandler);
        }
        else if (actualType == Byte.class)
        {
            cache.put(byte.class, typeHandler);
        }
        else if (actualType == Short.class)
        {
            cache.put(short.class, typeHandler);
        }
        else if (actualType == Integer.class)
        {
            cache.put(int.class, typeHandler);
        }
        else if (actualType == Long.class)
        {
            cache.put(long.class, typeHandler);
        }
        else if (actualType == Float.class)
        {
            cache.put(float.class, typeHandler);
        }
        else if (actualType == Double.class)
        {
            cache.put(double.class, typeHandler);
        }
        else if (actualType == Boolean.class)
        {
            cache.put(boolean.class, typeHandler);
        }
    }
}
