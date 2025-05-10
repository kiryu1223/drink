package io.github.kiryu1223.drink.base.transform;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTemplateExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface IAggregateMethods {

    SqlExpressionFactory getSqlExpressionFactory();

    default ISqlTemplateExpression count()
    {
        return count(null);
    }
    
    default ISqlTemplateExpression count(ISqlExpression expression) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        ISqlTemplateExpression templateExpression;
        if (expression == null) {
            templateExpression = factory.template(Collections.singletonList("COUNT(*)"), Collections.emptyList());
        }
        else {
            templateExpression = factory.template(Arrays.asList("COUNT(", ")"), Collections.singletonList(expression));
        }
        return templateExpression;
    }

    /**
     * SUM
     */
    default ISqlTemplateExpression sum(ISqlExpression expression) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.template(Arrays.asList("SUM(", ")"), Collections.singletonList(expression));
    }

    /**
     * AVG
     */
    default ISqlTemplateExpression avg(ISqlExpression expression) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.template(Arrays.asList("AVG(", ")"), Collections.singletonList(expression));
    }

    /**
     * MAX
     */
    default ISqlTemplateExpression max(ISqlExpression expression) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.template(Arrays.asList("MAX(", ")"), Collections.singletonList(expression));
    }

    /**
     * MIN
     */
    default ISqlTemplateExpression min(ISqlExpression expression) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.template(Arrays.asList("MIN(", ")"), Collections.singletonList(expression));
    }

    /**
     * GROUP_CONCAT
     */
    default ISqlTemplateExpression groupConcat(List<ISqlExpression> expressions) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> strings;
        List<ISqlExpression> args;
        //无分隔符
        if (expressions.size() == 1) {
            ISqlExpression property = expressions.get(0);
            strings = Arrays.asList("GROUP_CONCAT(", ")");
            args = Collections.singletonList(property);
        }
        //有分隔符
        else {
            ISqlExpression delimiter = expressions.get(0);
            ISqlExpression property = expressions.get(1);
            strings = Arrays.asList("GROUP_CONCAT(", " SEPARATOR ", ")");
            args = Arrays.asList(property, delimiter);
        }
        return factory.template(strings, args);
    }
}
