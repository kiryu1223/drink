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

    boolean useDialect();


    void setUseDialect(boolean use);

    @Override
    default ISqlDynamicColumnExpression copy(IConfig config) {
        IDialect dialect = config.getDisambiguation();
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlDynamicColumnExpression iSqlDynamicColumnExpression = factory.dynamicColumn(getColumn(), getColumnType(), getTableRefExpression());
        iSqlDynamicColumnExpression.setUseDialect(useDialect());
        return iSqlDynamicColumnExpression;
    }
}
