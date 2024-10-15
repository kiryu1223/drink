package io.github.kiryu1223.drink.core.builder;

import sun.misc.Unsafe;

import java.io.Serializable;
import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public class FastCreator<T>
{
    protected static final Unsafe unsafe;
    protected final Supplier<T> supplier;

    static
    {
        try
        {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        }
        catch (IllegalAccessException | NoSuchFieldException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected final Class<T> target;
    protected final boolean isAnonymousClass;
    //private final MethodHandles.Lookup lookup;

    public FastCreator(Class<T> target)
    {
        this.target = target;
        this.isAnonymousClass = target.isAnonymousClass();
        this.supplier = getCreator();
    }

    protected Supplier<T> unsafeCreator()
    {
        return () ->
        {
            try
            {
                return (T) unsafe.allocateInstance(target);
            }
            catch (InstantiationException e)
            {
                throw new RuntimeException(e);
            }
        };
    }

    protected Supplier<T> methodHandleCreator()
    {
        try
        {
            MethodType constructorType = MethodType.methodType(void.class);
            MethodHandles.Lookup caller = MethodHandles.lookup();
            MethodHandle constructorHandle = caller.findConstructor(target, constructorType);

            CallSite site = LambdaMetafactory.altMetafactory(caller,
                    "get",
                    MethodType.methodType(Supplier.class),
                    constructorHandle.type().generic(),
                    constructorHandle,
                    constructorHandle.type(), 1);
            return (Supplier<T>) site.getTarget().invokeExact();
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    protected Supplier<T> getCreator()
    {
        if (supplier != null) return supplier;
        if (isAnonymousClass)
        {
            return unsafeCreator();
        }
        else
        {
            return methodHandleCreator();
        }
    }

//    public Getter<Object, ?> getGetter(Class<?> propertyType, Method readMethod)
//    {
//        String getFunName = readMethod.getName();
//        //final MethodHandles.Lookup caller = MethodHandles.lookup();
//        MethodType methodType = MethodType.methodType(propertyType, target);
//        final CallSite site;
//
//        try
//        {
//            site = LambdaMetafactory.altMetafactory(lookup,
//                    "get",
//                    MethodType.methodType(Getter.class),
//                    methodType.erase().generic(),
//                    lookup.findVirtual(target, getFunName, MethodType.methodType(propertyType)),
//                    methodType, 1);
//            return (Getter<Object, ?>) site.getTarget().invokeExact();
//        }
//        catch (Throwable e)
//        {
//            throw new RuntimeException(e);
//        }
//    }

//    public Setter<Object> getSetter(Method writeMethod)
//    {
//        return getSetter(writeMethod.getParameterTypes()[0], writeMethod);
//    }

    public Setter<T> getSetter(Class<?> propertyType, Method writeMethod, final MethodHandles.Lookup lookup)
    {
        try
        {
            MethodType instantiatedMethodType = MethodType.methodType(void.class, target, propertyType);
            MethodHandle setter = lookup.findVirtual(target, writeMethod.getName(), MethodType.methodType(void.class, propertyType));
            CallSite set = LambdaMetafactory.metafactory(
                    lookup,
                    "set",
                    MethodType.methodType(Setter.class),
                    MethodType.methodType(void.class, Object.class, Object.class),
                    setter,
                    instantiatedMethodType
            );
            return (Setter<T>) set.getTarget().invokeExact();
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    public interface Getter<T, R> extends Serializable
    {
        R get(T t);
    }

    public interface Setter<T> extends Serializable
    {
        void set(T t, Object value);
    }
}
