package io.github.kiryu1223.drink.base.transform.method;

public interface MethodTransformer {

    IStringMethods getStringMethod();

    ITimeMethods getTimeMethod();

    INumberMethods getNumberMethod();

    IMathMethods getMathMethod();

    IAggregateMethods getAggregateMethod();

    IObjectsMethods getObjectsMethod();
}
