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
     * 获取分组列
     */
    ISqlColumnExpression  getGroupColumn();

    /**
     * 选择的列值集合
     */
    List<ISqlExpression> getSelectColumnValues();

    ISqlTableRefExpression getTableRefExpression();

    @Override
    default ISqlPivotExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<ISqlExpression> copySelectColumnValues = new ArrayList<>(getSelectColumnValues().size());
        for (ISqlExpression selectColumnValue : getSelectColumnValues()) {
            copySelectColumnValues.add(selectColumnValue.copy(config));
        }
        return factory.pivot(
                getAggregationColumn().copy(config),
                getGroupColumn().copy(config),
                copySelectColumnValues,
                getTableRefExpression().copy(config)
        );
    }
}
