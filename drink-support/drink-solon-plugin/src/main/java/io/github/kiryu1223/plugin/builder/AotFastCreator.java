package io.github.kiryu1223.plugin.builder;

import io.github.kiryu1223.drink.base.metaData.MetaDataCache;
import io.github.kiryu1223.drink.base.toBean.beancreator.DefaultBeanCreator;
import io.github.kiryu1223.drink.base.toBean.beancreator.ISetterCaller;
import org.noear.solon.core.runtime.NativeDetector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class AotFastCreator<T> extends DefaultBeanCreator<T>
{
    public AotFastCreator(Class<T> target)
    {
        super(target);
    }

    @Override
    public Supplier<T> initBeanCreator(Class<T> target)
    {
        if (target.isAnonymousClass())
        {
            return unsafeCreator(target);
        }
        else
        {
            // aot下我们不能使用方法句柄
            if (NativeDetector.inNativeImage())
            {
                MetaData metaData = MetaDataCache.getMetaData(target);
                Constructor<T> constructor = (Constructor<T>) metaData.getConstructor();
                return () ->
                {
                    try
                    {
                        return constructor.newInstance();
                    }
                    catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
                    {
                        throw new RuntimeException(e);
                    }
                };
            }
            else
            {
                return methodHandleCreator(target);
            }
        }
    }

    @Override
    protected ISetterCaller<T> initBeanSetter(String property)
    {
        if (target.isAnonymousClass())
        {
            return methodBeanSetter(property);
        }
        else
        {
            // aot下我们不能使用方法句柄
            if (NativeDetector.inNativeImage())
            {
                return methodBeanSetter(property);
            }
            else
            {
                return methodHandleBeanSetter(property);
            }
        }
    }
}
