package io.github.kiryu1223.drink.base.transform.method;

import io.github.kiryu1223.drink.base.expression.*;

import java.util.List;

public interface IStringMethods {
    /**
     * 数据库LIKE运算
     */
    ISqlBinaryExpression contains(ISqlExpression left, ISqlExpression right);

    /**
     * 数据库LIKE左匹配运算
     */
    ISqlBinaryExpression startsWith(ISqlExpression left, ISqlExpression right);

    /**
     * 数据库LIKE右匹配运算
     */
    ISqlBinaryExpression endsWith(ISqlExpression left, ISqlExpression right);

    /**
     * 数据库字符串长度函数
     */
    ISqlTemplateExpression length(ISqlExpression thiz);

    /**
     * 数据库字符串转大写函数
     */
    ISqlTemplateExpression toUpperCase(ISqlExpression thiz);

    /**
     * 数据库字符串转小写函数
     */
    ISqlTemplateExpression toLowerCase(ISqlExpression thiz);

    /**
     * 数据库字符串拼接函数
     */
    ISqlTemplateExpression concat(ISqlExpression left, ISqlExpression right);

    /**
     * 数据库字符串左右去空格函数
     */
    ISqlTemplateExpression trim(ISqlExpression thiz);

    /**
     * 数据库判断字符串是否为空表达式
     */
    ISqlExpression isEmpty(ISqlExpression thiz);

    /**
     * 数据库字符串查找索引函数
     */
    ISqlTemplateExpression indexOf(ISqlExpression thisStr, ISqlExpression subStr);

    /**
     * 数据库字符串查找索引函数
     */
    ISqlTemplateExpression indexOf(ISqlExpression thisStr, ISqlExpression subStr, ISqlExpression fromIndex);

    /**
     * 数据库字符串替换函数
     */
    ISqlTemplateExpression replace(ISqlExpression thisStr, ISqlExpression oldStr, ISqlExpression newStr);

    /**
     * 数据库字符串截取函数
     */
    ISqlTemplateExpression substring(ISqlExpression thisStr, ISqlExpression beginIndex);

    /**
     * 数据库字符串截取函数
     */
    ISqlTemplateExpression substring(ISqlExpression thisStr, ISqlExpression beginIndex, ISqlExpression endIndex);

    /**
     * 数据库字符串连接函数
     */
    ISqlTemplateExpression joinArray(ISqlExpression delimiter, List<ISqlExpression> elements);

    /**
     * 数据库字符串连接函数
     */
    ISqlTemplateExpression joinList(ISqlExpression delimiter, ISqlExpression elements);
}
