package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;

public interface ISqlDynamicColumnExpression extends ISqlExpression {

    ISqlTableRefExpression getTableRefExpression();

    String getColumn();

    Class<?> getColumnType();

    default Class<?> getType() {
        return getColumnType();
    }

    @Override
    default ISqlDynamicColumnExpression copy(IConfig config) {
        IDialect dialect = config.getDisambiguation();
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.dynamicColumn(getColumn(), getColumnType(), getTableRefExpression());
    }
}
