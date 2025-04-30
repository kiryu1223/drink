package io.github.kiryu1223.drink.base.transform.method;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.transform.Transform;
import io.github.kiryu1223.drink.base.transform.method.impl.*;

public class DefaultMethodTransform extends Transform implements MethodTransformer {

    public DefaultMethodTransform(IConfig config) {
        super(config);
    }

    @Override
    public IStringMethods getStringMethod() {
        return new StringMethods(config);
    }

    @Override
    public ITimeMethods getTimeMethod() {
        return new TimeMethods(config);
    }

    @Override
    public INumberMethods getNumberMethod() {
        return new NumberMethods(config);
    }

    @Override
    public IMathMethods getMathMethod() {
        return new MathMethods(config);
    }

    @Override
    public IAggregateMethods getAggregateMethod() {
        return new AggregateMethods(config);
    }

    @Override
    public IObjectsMethods getObjectsMethod() {
        return new ObjectsMethods(config);
    }
}
