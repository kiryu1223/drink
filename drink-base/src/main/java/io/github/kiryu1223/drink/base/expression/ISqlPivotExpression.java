package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 列转行表达式
 */
public interface ISqlPivotExpression extends ISqlExpression {
    /**
     * 获取聚合列
     */
    ISqlExpression getAggregationColumn();

    /**
     * 获取聚合列的返回类型
     */
    Class<?> getAggregationType();

    /**
     * 获取需要转换的列
     */
    ISqlColumnExpression getTransColumn();

    /**
     * 获取需要转换的列值
     */
    List<ISqlExpression> getTransColumnValues();

    ISqlTableRefExpression getTempRefExpression();
    ISqlTableRefExpression getPivotRefExpression();

    @Override
    default ISqlPivotExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<ISqlExpression> copySelectColumnValues = new ArrayList<>(getTransColumnValues().size());
        for (ISqlExpression selectColumnValue : getTransColumnValues()) {
            copySelectColumnValues.add(selectColumnValue.copy(config));
        }
        return factory.pivot(
                getAggregationColumn().copy(config),
                getAggregationType(),
                getTransColumn().copy(config),
                copySelectColumnValues,
                getTempRefExpression().copy(config)
        );
    }
}
