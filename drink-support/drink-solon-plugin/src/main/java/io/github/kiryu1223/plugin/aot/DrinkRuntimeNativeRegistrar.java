package io.github.kiryu1223.plugin.aot;

import io.github.kiryu1223.drink.ext.NoConverter;
import io.github.kiryu1223.plugin.configuration.DrinkProperties;
import org.noear.solon.annotation.Component;
import org.noear.solon.aot.RuntimeNativeMetadata;
import org.noear.solon.aot.RuntimeNativeRegistrar;
import org.noear.solon.aot.hint.MemberCategory;
import org.noear.solon.core.AppContext;


public class DrinkRuntimeNativeRegistrar implements RuntimeNativeRegistrar
{
    @Override
    public void register(AppContext context, RuntimeNativeMetadata metadata)
    {
        metadata.registerReflection(DrinkProperties.class, MemberCategory.DECLARED_FIELDS, MemberCategory.INVOKE_PUBLIC_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
        metadata.registerReflection(NoConverter.class,MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,MemberCategory.DECLARED_FIELDS,MemberCategory.INVOKE_PUBLIC_METHODS);
    }
}
