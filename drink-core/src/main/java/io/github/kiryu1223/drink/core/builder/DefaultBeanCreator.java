package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.core.visitor.ExpressionUtil;
import io.github.kiryu1223.drink.exception.DrinkException;
import sun.misc.Unsafe;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    protected ISetterCaller<T> initBeanSetter(String property)
    {
        if (target.isAnonymousClass())
        {
            return methodBeanSetter(property);
        }
        else
        {
            return methodHandleBeanSetter(property);
        }
    }

    protected ISetterCaller<T> methodBeanSetter(String property)
    {
        PropertyMetaData propertyMetaData = MetaDataCache.getMetaData(target).getPropertyMetaDataByFieldName(property);
        Method setter = propertyMetaData.getSetter();
        return (t, v) -> setter.invoke(t, v);
    }

    protected ISetterCaller<T> methodHandleBeanSetter(String property)
    {
        PropertyMetaData propertyMetaData = MetaDataCache.getMetaData(target).getPropertyMetaDataByFieldName(property);
        Class<?> propertyType = propertyMetaData.getType();

        MethodHandles.Lookup caller = MethodHandles.lookup();
        Method writeMethod = propertyMetaData.getSetter();
        MethodType setter = MethodType.methodType(writeMethod.getReturnType(), propertyType);

        Class<?> lambdaPropertyType = ExpressionUtil.upperClass(propertyType);
        String getFunName = writeMethod.getName();
        try
        {

            //()->{bean.setxxx(propertyType)}
            MethodType instantiatedMethodType = MethodType.methodType(void.class, target, lambdaPropertyType);
            MethodHandle targetHandle = caller.findVirtual(target, getFunName, setter);
            MethodType samMethodType = MethodType.methodType(void.class, Object.class, Object.class);
            CallSite site = LambdaMetafactory.metafactory(
                    caller,
                    "apply",
                    MethodType.methodType(IVoidSetter.class),
                    samMethodType,
                    targetHandle,
                    instantiatedMethodType
            );

            IVoidSetter<Object, Object> objectPropertyVoidSetter = (IVoidSetter<Object, Object>) site.getTarget().invokeExact();
            return objectPropertyVoidSetter::apply;
        }
        catch (Throwable e)
        {
            throw new DrinkException(e);
        }
    }
}
