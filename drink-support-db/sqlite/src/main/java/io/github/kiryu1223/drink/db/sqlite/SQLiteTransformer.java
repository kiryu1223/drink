package io.github.kiryu1223.drink.db.sqlite;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.transform.Transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.*;

public class SQLiteTransformer implements Transformer
{
    private final SqlExpressionFactory factory;

    public SQLiteTransformer(IConfig config)
    {
        //this.config = config;
        factory = config.getSqlExpressionFactory();
    }
    
    @Override
    public SqlExpressionFactory getSqlExpressionFactory()
    {
        return factory;
    }

    private String getUnit(Class<?> type)
    {
        if (isChar(type) || isString(type)) {
            return "TEXT";
        }
        else if (isByte(type) || isShort(type) || isInt(type) || isLong(type)) {
            return "INTEGER";
        }
        else if (isFloat(type) || isDouble(type) || isDecimal(type)) {
            return "REAL";
        }
        else {
            throw new DrinkException("不支持的Java类型:" + type.getName());
        }
    }

    @Override
    public ISqlExpression typeCast(ISqlExpression value, Class<?> type)
    {
        List<String> templates = Arrays.asList("CAST("," AS " + getUnit(type) + ")");
        List<ISqlExpression> sqlExpressions = Collections.singletonList(value);
        return factory.template(templates, sqlExpressions);
    }

    public ISqlExpression If(ISqlExpression cond, ISqlExpression truePart, ISqlExpression falsePart) {
        return factory.template(Arrays.asList("IIF(", ",", ",", ")"), Arrays.asList(cond, truePart, falsePart));
    }

    public ISqlTemplateExpression groupConcat(List<ISqlExpression> expressions) {
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
            strings = Arrays.asList("GROUP_CONCAT(", ",", ")");
            args = Arrays.asList(property, delimiter);
        }
        return factory.template(strings, args);
    }

    public ISqlTemplateExpression random() {
        return factory.template(Collections.singletonList("ABS(RANDOM() / 10000000000000000000.0)"), Collections.emptyList());
    }

    public ISqlBinaryExpression contains(ISqlExpression left, ISqlExpression right) {
        ISqlTemplateExpression function = factory.template(Arrays.asList("('%'||", "||'%')"), Collections.singletonList(right));
        return factory.binary(SqlOperator.LIKE, left, function);
    }

    public ISqlBinaryExpression startsWith(ISqlExpression left, ISqlExpression right) {
        return factory.binary(SqlOperator.LIKE, left, factory.template(Arrays.asList("(", "||'%')"), Collections.singletonList(right)));
    }

    public ISqlBinaryExpression endsWith(ISqlExpression left, ISqlExpression right) {
        return factory.binary(SqlOperator.LIKE, left, factory.template(Arrays.asList("('%'||", ")"), Collections.singletonList(right)));
    }

    public ISqlTemplateExpression length(ISqlExpression thiz) {
        return factory.template(Arrays.asList("LENGTH(", ")"), Collections.singletonList(thiz));
    }

    public ISqlTemplateExpression concat(ISqlExpression left, ISqlExpression right) {
        return factory.template(Arrays.asList("(", "||", ")"), Arrays.asList(left, right));
    }

    public ISqlTemplateExpression indexOf(ISqlExpression thisStr, ISqlExpression subStr, ISqlExpression fromIndex) {
        return factory.template(Arrays.asList("(INSTR(SUBSTR(", ",", " + 1),", ") + ", ")"), Arrays.asList(thisStr, fromIndex, subStr, fromIndex));
    }

    public ISqlTemplateExpression joinArray(ISqlExpression delimiter, List<ISqlExpression> elements) {
        List<String> functions = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>(1 + elements.size());
        functions.add("(");
        for (int i = 0; i < elements.size(); i++) {
            sqlExpressions.add(elements.get(i));
            if (i < elements.size() - 1) {
                functions.add("||");
                sqlExpressions.add(delimiter);
                functions.add("||");
            }
        }
        functions.add(")");
        return factory.template(functions, sqlExpressions);
    }

    public ISqlTemplateExpression joinList(ISqlExpression delimiter, ISqlExpression elements) {
        ISqlCollectedValueExpression expression = (ISqlCollectedValueExpression) elements;
        List<Object> collection = new ArrayList<>(expression.getCollection());
        List<String> functions = new ArrayList<>(collection.size() * 2);
        List<ISqlExpression> sqlExpressions = new ArrayList<>(collection.size() * 2);
        functions.add("(");
        for (int i = 0; i < collection.size(); i++) {
            sqlExpressions.add(factory.value(collection.get(i)));
            if (i < collection.size() - 1) {
                functions.add("||");
                sqlExpressions.add(delimiter);
                functions.add("||");
            }
        }
        functions.add(")");
        return factory.template(functions, sqlExpressions);
    }
}
