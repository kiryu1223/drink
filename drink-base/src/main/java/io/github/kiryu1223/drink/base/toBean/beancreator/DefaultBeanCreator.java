package io.github.kiryu1223.drink.base.toBean.beancreator;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import sun.misc.Unsafe;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public class DefaultBeanCreator<T> extends AbsBeanCreator<T> {
    protected static final Unsafe unsafe;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public DefaultBeanCreator(Class<T> target, IConfig config) {
        super(config, target);
    }

    @Override
    protected Supplier<T> initBeanCreator(Class<T> target) {
        if (target.isAnonymousClass()) {
            return unsafeCreator(target);
        }
        else {
            return methodHandleCreator(target);
        }
    }

    protected Supplier<T> unsafeCreator(Class<T> target) {
        return () ->
        {
            try {
                return (T) unsafe.allocateInstance(target);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        };
    }

    protected Supplier<T> methodHandleCreator(Class<T> target) {
        try {
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
        } catch (Throwable e) {
            System.out.println(target);
            throw new DrinkException(e);
        }
    }

    protected ISetterCaller<T> initBeanSetter(String fieldName) {
        if (target.isAnonymousClass()) {
            return methodBeanSetter(fieldName);
        }
        else {
            return methodHandleBeanSetter(fieldName);
        }
    }

    protected IGetterCaller<T,?> initBeanGetter(String fieldName) {
        if (target.isAnonymousClass()) {
            return methodBeanGetter(fieldName);
        }
        else {
            return methodHandleBeanGetter(fieldName);
        }
    }

    protected ISetterCaller<T> methodBeanSetter(String property) {
        FieldMetaData propertyMetaData = config.getMetaData(target).getFieldMetaDataByFieldName(property);
        Method setter = propertyMetaData.getSetter();
        return (t, v) -> setter.invoke(t, v);
    }

    protected ISetterCaller<T> methodHandleBeanSetter(String property) {
        FieldMetaData propertyMetaData = config.getMetaData(target).getFieldMetaDataByFieldName(property);
        Class<?> propertyType = propertyMetaData.getType();

        MethodHandles.Lookup caller = MethodHandles.lookup();
        Method writeMethod = propertyMetaData.getSetter();
        MethodType setter = MethodType.methodType(writeMethod.getReturnType(), propertyType);

        Class<?> lambdaPropertyType = upperClass(propertyType);
        String getFunName = writeMethod.getName();
        try {

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
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    protected IGetterCaller<T, ?> methodBeanGetter(String property) {
        FieldMetaData propertyMetaData = config.getMetaData(target).getFieldMetaDataByFieldName(property);
        Method getter = propertyMetaData.getGetter();
        return (t) -> getter.invoke(t);
    }

    protected IGetterCaller<T, ?> methodHandleBeanGetter(String fieldName) {
//        Class<?> propertyType = prop.getPropertyType();
//        Method readMethod = prop.getReadMethod();
        MetaData metaData = config.getMetaData(target);
        FieldMetaData fieldMetaData = metaData.getFieldMetaDataByFieldName(fieldName);
        Class<?> fieldType = fieldMetaData.getType();
        String getFunName = fieldMetaData.getGetter().getName();
        MethodHandles.Lookup caller = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(fieldType, target);

        try {
            CallSite site = LambdaMetafactory.altMetafactory(
                    caller,
                    "apply",
                    MethodType.methodType(IGetterCaller.class),
                    methodType.erase().generic(),
                    caller.findVirtual(
                            target,
                            getFunName,
                            MethodType.methodType(fieldType)
                    ),
                    methodType,
                    1);
            return (IGetterCaller<T, ?>) site.getTarget().invokeExact();
        } catch (Throwable e) {
            throw new DrinkException(e);
        }
    }

    private Class<?> upperClass(Class<?> c) {
        if (c.isPrimitive()) {
            if (c == Character.TYPE) {
                return Character.class;
            }
            else if (c == Byte.TYPE) {
                return Byte.class;
            }
            else if (c == Short.TYPE) {
                return Short.class;
            }
            else if (c == Integer.TYPE) {
                return Integer.class;
            }
            else if (c == Long.TYPE) {
                return Long.class;
            }
            else if (c == Float.TYPE) {
                return Float.class;
            }
            else if (c == Double.TYPE) {
                return Double.class;
            }
            else if (c == Boolean.TYPE) {
                return Boolean.class;
            }
            else {
                return Void.class;
            }
        }
        else {
            return c;
        }
    }
}
