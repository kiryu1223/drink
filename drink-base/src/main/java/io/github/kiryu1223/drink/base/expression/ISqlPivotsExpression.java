package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;
import java.util.stream.Collectors;

public interface ISqlPivotsExpression extends ISqlExpression {
    List<ISqlPivotExpression> getPivots();

    default void addPivot(ISqlPivotExpression pivot) {
        getPivots().add(pivot);
    }

    @Override
    default ISqlPivotsExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.pivots(getPivots().stream().map(pivot -> pivot.copy(config)).collect(Collectors.toList()));
    }
}
