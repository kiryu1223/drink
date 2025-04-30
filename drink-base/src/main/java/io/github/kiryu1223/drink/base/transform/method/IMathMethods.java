package io.github.kiryu1223.drink.base.transform.method;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTemplateExpression;

public interface IMathMethods {
    /**
     * 数据库atan2函数
     */
    ISqlTemplateExpression atan2(ISqlExpression arg1, ISqlExpression arg2);

    /**
     * 数据库ceil函数
     */
    ISqlTemplateExpression ceil(ISqlExpression arg);

    /**
     * 数据库degrees函数
     */
    ISqlTemplateExpression toDegrees(ISqlExpression arg);

    /**
     * 数据库radians函数
     */
    ISqlTemplateExpression toRadians(ISqlExpression arg);

    /**
     * 数据库log函数
     */
    ISqlTemplateExpression log(ISqlExpression arg);

    /**
     * 数据库log10函数
     */
    ISqlTemplateExpression log10(ISqlExpression arg);

    /**
     * 数据库random函数
     */
    ISqlTemplateExpression random(IConfig config);

    /**
     * 数据库round函数
     */
    ISqlTemplateExpression round(ISqlExpression arg);
}
