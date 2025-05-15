package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.Map;

public interface ISqlTableRefExpression extends ISqlExpression {
    String getName();
    String getDisPlayName();
    @Override
    default ISqlTableRefExpression copy(IConfig config) {
        return this;
    }
}
