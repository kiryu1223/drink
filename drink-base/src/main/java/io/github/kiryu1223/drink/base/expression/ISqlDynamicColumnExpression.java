package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

public interface ISqlDynamicColumnExpression extends ISqlExpression {
    void setTableAsName(AsName tableAsName);

    AsName getTableAsName();

    String getColumn();

    Class<?> getType();

    @Override
    default ISqlDynamicColumnExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.dynamicColumn(getColumn(), getType(), getTableAsName());
    }
}
