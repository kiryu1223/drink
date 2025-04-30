package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

public interface ISqlUnionsExpression extends ISqlExpression {
    List<ISqlUnionExpression> getUnions();

    void addUnion(ISqlUnionExpression union);

    @Override
    default ISqlUnionsExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlUnionsExpression unions = factory.unions();
        for (ISqlUnionExpression union : getUnions()) {
            unions.addUnion(union.copy(config));
        }
        return unions;
    }
}
