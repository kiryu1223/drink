package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

public interface ISqlWithsExpression extends ISqlExpression {
    List<ISqlWithExpression> getWiths();

    void addWith(ISqlWithExpression withExpression);

    @Override
    default ISqlWithsExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlWithsExpression withs = factory.withs();
        for (ISqlWithExpression with : getWiths()) {
            withs.addWith(with);
        }
        return withs;
    }
}
