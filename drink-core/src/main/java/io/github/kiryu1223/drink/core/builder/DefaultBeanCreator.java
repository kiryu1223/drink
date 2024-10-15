package io.github.kiryu1223.drink.core.builder;

import sun.misc.Unsafe;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.util.function.Supplier;

public class DefaultBeanCreator<T> extends AbsBeanCreator<T>
{
    protected static final Unsafe unsafe;

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

    public DefaultBeanCreator(Class<T> target)
    {
        super(target);
    }

    @Override
    protected Supplier<T> initBeanCreator(Class<T> target)
    {
        if (target.isAnonymousClass())
        {
            return unsafeCreator(target);
        }
        else
        {
            return methodHandleCreator(target);
        }
    }

    protected Supplier<T> unsafeCreator(Class<T> target)
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

    protected Supplier<T> methodHandleCreator(Class<T> target)
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
}
