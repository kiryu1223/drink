package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.SqlOption;
import io.github.kiryu1223.drink.base.SqlOptions;

import java.util.List;

public interface ISqlWarpExpression extends ISqlExpression {

    ISqlExpression getExpression();

    String getFilterId();

    default boolean useIt() {
        SqlOption option = SqlOptions.getOption();
        if (option.isIgnoreFilterAll()) {
            return false;
        }
        List<String> ignoreFilterIds = option.getIgnoreFilterIds();
        return !ignoreFilterIds.contains(getFilterId());
    }

    @Override
    default ISqlWarpExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.warp(getExpression(), getFilterId());
    }
}
