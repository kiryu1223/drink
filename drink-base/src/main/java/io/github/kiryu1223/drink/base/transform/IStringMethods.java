package io.github.kiryu1223.drink.base.transform;

import io.github.kiryu1223.drink.base.expression.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface IStringMethods {

    SqlExpressionFactory getSqlExpressionFactory();

    /**
     * 数据库LIKE运算
     */
    default ISqlBinaryExpression contains(ISqlExpression left, ISqlExpression right) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> functions = Arrays.asList("CONCAT('%',", ",'%')");
        ISqlTemplateExpression function = factory.template(functions, Collections.singletonList(right));
        return factory.binary(SqlOperator.LIKE, left, function);
    }

    /**
     * 数据库LIKE左匹配运算
     */
    default ISqlBinaryExpression startsWith(ISqlExpression left, ISqlExpression right) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> functions = Arrays.asList("CONCAT(", ",'%')");
        List<ISqlExpression> args = Collections.singletonList(right);
        return factory.binary(SqlOperator.LIKE, left, factory.template(functions, args));
    }

    /**
     * 数据库LIKE右匹配运算
     */
    default ISqlBinaryExpression endsWith(ISqlExpression left, ISqlExpression right) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> functions = Arrays.asList("CONCAT('%',", ")");
        List<ISqlExpression> args = Collections.singletonList(right);
        return factory.binary(SqlOperator.LIKE, left, factory.template(functions, args));
    }

    /**
     * 数据库字符串长度函数
     */
    default ISqlTemplateExpression length(ISqlExpression thiz) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> functions = Arrays.asList("CHAR_LENGTH(", ")");
        return factory.template(functions, Collections.singletonList(thiz));
    }

    default ISqlTemplateExpression toUpperCase(ISqlExpression thiz) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> functions = Arrays.asList("UPPER(", ")");
        return factory.template(functions, Collections.singletonList(thiz));
    }

    default ISqlTemplateExpression toLowerCase(ISqlExpression thiz) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> functions = Arrays.asList("LOWER(", ")");
        return factory.template(functions, Collections.singletonList(thiz));
    }

    /**
     * 数据库字符串拼接函数
     */
    default ISqlTemplateExpression concat(ISqlExpression left, ISqlExpression right) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> functions = Arrays.asList("CONCAT(", ",", ")");
        return factory.template(functions, Arrays.asList(left, right));
    }

    default ISqlTemplateExpression trim(ISqlExpression thiz) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> functions = Arrays.asList("TRIM(", ")");
        return factory.template(functions, Collections.singletonList(thiz));
    }

    /**
     * 数据库判断字符串是否为空表达式
     */
    default ISqlExpression isEmpty(ISqlExpression thiz) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        return factory.parens(factory.binary(SqlOperator.EQ, length(thiz), factory.constString("0")));
    }

    /**
     * 数据库字符串查找索引函数
     */
    default ISqlTemplateExpression indexOf(ISqlExpression thisStr, ISqlExpression subStr) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> functions = Arrays.asList("INSTR(", ",", ")");
        List<ISqlExpression> sqlExpressions = Arrays.asList(thisStr, subStr);
        return factory.template(functions, sqlExpressions);
    }

    /**
     * 数据库字符串查找索引函数
     */
    default ISqlTemplateExpression indexOf(ISqlExpression thisStr, ISqlExpression subStr, ISqlExpression fromIndex) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> functions = Arrays.asList("LOCATE(", ",", ",", ")");
        List<ISqlExpression> sqlExpressions = Arrays.asList(subStr, thisStr, fromIndex);
        return factory.template(functions, sqlExpressions);
    }

    /**
     * 数据库字符串替换函数
     */
    default ISqlTemplateExpression replace(ISqlExpression thisStr, ISqlExpression oldStr, ISqlExpression newStr) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> functions = Arrays.asList("REPLACE(", ",", ",", ")");
        List<ISqlExpression> sqlExpressions = Arrays.asList(thisStr, oldStr, newStr);
        return factory.template(functions, sqlExpressions);
    }

    /**
     * 数据库字符串截取函数
     */
    default ISqlTemplateExpression substring(ISqlExpression thisStr, ISqlExpression beginIndex) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> functions = Arrays.asList("SUBSTR(", ",", ")");
        List<ISqlExpression> sqlExpressions = Arrays.asList(thisStr, beginIndex);
        return factory.template(functions, sqlExpressions);
    }

    /**
     * 数据库字符串截取函数
     */
    default ISqlTemplateExpression substring(ISqlExpression thisStr, ISqlExpression beginIndex, ISqlExpression endIndex) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> functions = Arrays.asList("SUBSTR(", ",", ",", ")");
        List<ISqlExpression> sqlExpressions = Arrays.asList(thisStr, beginIndex, endIndex);
        return factory.template(functions, sqlExpressions);
    }

    /**
     * 数据库字符串连接函数
     */
    default ISqlTemplateExpression joinArray(ISqlExpression delimiter, List<ISqlExpression> elements) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> functions = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>(1 + elements.size());
        sqlExpressions.add(delimiter);
        sqlExpressions.addAll(elements);
        functions.add("CONCAT_WS(");
        functions.add(",");
        for (int i = 0; i < sqlExpressions.size(); i++) {
            if (i < elements.size() - 1) functions.add(",");
        }
        functions.add(")");
        return factory.template(functions, sqlExpressions);
    }

    /**
     * 数据库字符串连接函数
     */
    default ISqlTemplateExpression joinList(ISqlExpression delimiter, ISqlExpression elements) {
        if (!(elements instanceof ISqlCollectedValueExpression)) {
            throw new RuntimeException("String.join()的第二个参数必须是java中能获取到的");
        }
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> functions = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = Arrays.asList(delimiter, elements);
        functions.add("CONCAT_WS(");
        functions.add(",");
        functions.add(")");
        return factory.template(functions, sqlExpressions);
    }
}
