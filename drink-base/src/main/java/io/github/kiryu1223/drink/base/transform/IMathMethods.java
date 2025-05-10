package io.github.kiryu1223.drink.base.transform;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTemplateExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface IMathMethods {

    SqlExpressionFactory getSqlExpressionFactory();
    /**
     * 数据库atan2函数
     */
    default ISqlTemplateExpression atan2(ISqlExpression arg1, ISqlExpression arg2) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> function=Arrays.asList("ATAN2(", ",", ")");
        return factory.template(function, Arrays.asList(arg1, arg2));
    }

    /**
     * 数据库ceil函数
     */
    default ISqlTemplateExpression ceil(ISqlExpression arg) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> function= Arrays.asList("CEIL(", ")");
        return factory.template(function, Collections.singletonList(arg));
    }

    /**
     * 数据库degrees函数
     */
    default ISqlTemplateExpression toDegrees(ISqlExpression arg) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> function= Arrays.asList("DEGREES(", ")");
        return factory.template(function, Collections.singletonList(arg));
    }

    /**
     * 数据库radians函数
     */
    default ISqlTemplateExpression toRadians(ISqlExpression arg) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> function= Arrays.asList("RADIANS(", ")");
        return factory.template(function, Collections.singletonList(arg));
    }

    /**
     * 数据库log函数
     */
    default ISqlTemplateExpression log(ISqlExpression arg) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> function = Arrays.asList("LN(", ")");
        return factory.template(function, Collections.singletonList(arg));
    }

    /**
     * 数据库log10函数
     */
    default ISqlTemplateExpression log10(ISqlExpression arg) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> function = Arrays.asList("LOG10(", ")");
        return factory.template(function, Collections.singletonList(arg));
    }

    /**
     * 数据库random函数
     */
    default ISqlTemplateExpression random() {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> function = Collections.singletonList("RAND()");
        return factory.template(function, Collections.emptyList());
    }

    /**
     * 数据库round函数
     */
    default ISqlTemplateExpression round(ISqlExpression arg) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String>  function = Arrays.asList("ROUND(", ")");
        return factory.template(function, Collections.singletonList(arg));
    }
}
