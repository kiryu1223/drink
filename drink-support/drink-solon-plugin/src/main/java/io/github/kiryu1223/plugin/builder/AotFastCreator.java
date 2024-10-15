package io.github.kiryu1223.plugin.builder;

import io.github.kiryu1223.drink.core.builder.DefaultBeanCreator;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
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
}
