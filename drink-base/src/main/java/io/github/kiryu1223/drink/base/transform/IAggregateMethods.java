package io.github.kiryu1223.drink.base.transform;

import io.github.kiryu1223.drink.base.expression.ISqlConstStringExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTemplateExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface IAggregateMethods {

    SqlExpressionFactory getSqlExpressionFactory();

    default ISqlTemplateExpression count() {
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
    default ISqlTemplateExpression groupJoin(List<ISqlExpression> expressions) {
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

    default ISqlTemplateExpression over(List<ISqlExpression> expressions) {
        return over(expressions, true);
    }

    default ISqlTemplateExpression over(List<ISqlExpression> expressions, boolean rows) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> strings;
        List<ISqlExpression> args;
        if (expressions.isEmpty()) {
            strings = Collections.singletonList("OVER ()");
            args = Collections.emptyList();
        }
        else if (expressions.size() == 1) {
            ISqlExpression partition = expressions.get(0);
            strings = Arrays.asList("OVER (PARTITION BY", ")");
            args = Collections.singletonList(partition);
        }
        else if (expressions.size() == 2) {
            ISqlExpression partition = expressions.get(0);
            ISqlExpression orderBy = expressions.get(1);
            strings = Arrays.asList("OVER (PARTITION BY", "ORDER BY", ")");
            args = Arrays.asList(partition, orderBy);
        }
        else {
            ISqlExpression partition = expressions.get(0);
            ISqlExpression orderBy = expressions.get(1);
            ISqlExpression start = expressions.get(2);
            ISqlExpression end = expressions.get(3);
            strings = Arrays.asList("OVER (PARTITION BY", "ORDER BY", (rows ? "ROWS" : "RANGE") + " BETWEEN", "AND", ")");
            args = Arrays.asList(partition, orderBy, start, end);
        }
        return factory.template(strings, args);
    }

    default ISqlConstStringExpression rowNumber() {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.constString("ROW_NUMBER()");
    }
}
