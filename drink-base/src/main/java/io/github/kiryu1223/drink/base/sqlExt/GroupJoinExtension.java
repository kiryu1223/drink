package io.github.kiryu1223.drink.base.sqlExt;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;

import java.lang.reflect.Method;
import java.util.List;

public class GroupJoinExtension extends BaseSqlExtension{
    @Override
    public ISqlExpression parse(IConfig config, Method sqlFunc, List<ISqlExpression> args) {
        return config.getTransformer().groupConcat(args);
    }
}
