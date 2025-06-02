package io.github.kiryu1223.drink.base.transform;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTemplateExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface IMathMethods {

    SqlExpressionFactory getSqlExpressionFactory();

    default ISqlTemplateExpression abs(ISqlExpression arg)
    {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.template(Arrays.asList("ABS(", ")"), Collections.singletonList(arg));
    }

    default ISqlTemplateExpression sin(ISqlExpression arg)
    {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.template(Arrays.asList("SIN(", ")"), Collections.singletonList(arg));
    }

    default ISqlTemplateExpression asin(ISqlExpression arg)
    {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.template(Arrays.asList("ASIN(", ")"), Collections.singletonList(arg));
    }

    default ISqlTemplateExpression cos(ISqlExpression arg)
    {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.template(Arrays.asList("COS(", ")"), Collections.singletonList(arg));
    }

    default ISqlTemplateExpression acos(ISqlExpression arg)
    {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.template(Arrays.asList("ACOS(", ")"), Collections.singletonList(arg));
    }

    default ISqlTemplateExpression tan(ISqlExpression arg)
    {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.template(Arrays.asList("TAN(", ")"), Collections.singletonList(arg));
    }

    default ISqlTemplateExpression atan(ISqlExpression arg)
    {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.template(Arrays.asList("ATAN(", ")"), Collections.singletonList(arg));
    }


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

    default ISqlTemplateExpression exp(ISqlExpression arg) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.template(Arrays.asList("EXP(", ")"), Collections.singletonList(arg));
    }

    default ISqlTemplateExpression floor(ISqlExpression arg) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.template(Arrays.asList("FLOOR(", ")"), Collections.singletonList(arg));
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

    default ISqlTemplateExpression pow(ISqlExpression arg1,ISqlExpression arg2) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.template(Arrays.asList("POWER(", ",", ")"), Arrays.asList(arg1, arg2));
    }

    default ISqlTemplateExpression signum(ISqlExpression arg) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.template(Arrays.asList("SIGN(", ")"), Collections.singletonList(arg));
    }

    default ISqlTemplateExpression sqrt(ISqlExpression arg) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.template(Arrays.asList("SQRT(", ")"), Collections.singletonList(arg));
    }
}
