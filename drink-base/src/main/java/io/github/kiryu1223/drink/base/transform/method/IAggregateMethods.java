package io.github.kiryu1223.drink.base.transform.method;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTemplateExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface IAggregateMethods {
    /**
     * COUNT
     */
    ISqlTemplateExpression count(ISqlExpression expression);

    /**
     * COUNT
     */
    default ISqlTemplateExpression count() {
        return count(null);
    }

    /**
     * SUM
     */
    ISqlTemplateExpression sum(ISqlExpression expression);

    /**
     * AVG
     */
    ISqlTemplateExpression avg(ISqlExpression expression);

    /**
     * MAX
     */
    ISqlTemplateExpression max(ISqlExpression expression);

    /**
     * MIN
     */
    ISqlTemplateExpression min(ISqlExpression expression);

    /**
     * GROUP_CONCAT
     */
    ISqlTemplateExpression groupConcat(List<ISqlExpression> expressions);
}
