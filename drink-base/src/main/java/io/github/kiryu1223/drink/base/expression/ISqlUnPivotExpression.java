package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.List;

public interface ISqlUnPivotExpression extends ISqlTableExpression {
    ISqlQueryableExpression getQueryableExpression();

    String getNewNameColumnName();

    String getNewValueColumnName();

    Class<?> getNewValueColumnType();

    List<ISqlColumnExpression> getTransColumns();

    ISqlTableRefExpression getUnpivotRefExpression();

    ISqlTableRefExpression getTempRefExpression();

    @Override
    default Class<?> getMainTableClass() {
        return getQueryableExpression().getMainTableClass();
    }

    String unPivotStyle(IConfig config, List<SqlValue> values);

    String unionStyle(IConfig config, List<SqlValue> values);

    @Override
    default ISqlUnPivotExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.unPivot(
                getQueryableExpression().copy(config),
                getNewNameColumnName(),
                getNewValueColumnName(),
                getNewValueColumnType(),
                getTransColumns(),
                getTempRefExpression(),
                getUnpivotRefExpression()
        );
    }
}
