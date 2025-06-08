package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 列转行表达式
 */
public interface ISqlPivotExpression extends ISqlTableExpression
{

    ISqlQueryableExpression getQueryableExpression();

    /**
     * 获取聚合列
     */
    ISqlTemplateExpression getAggregationColumn();

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
    Collection<Object> getTransColumnValues();

    ISqlTableRefExpression getPivotRefExpression();

    ISqlTableRefExpression getTempRefExpression();

    @Override
    default ISqlPivotExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.pivot(
                getQueryableExpression().copy(config),
                getAggregationColumn().copy(config),
                getAggregationType(),
                getTransColumn().copy(config),
                getTransColumnValues(),
                getTempRefExpression().copy(config),
                getPivotRefExpression().copy(config)
        );
    }
}
