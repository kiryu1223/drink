package io.github.kiryu1223.drink.db.oracle;

import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.transform.Transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.*;

public class OracleTransformer implements Transformer
{
    //private final IConfig config;
    private final SqlExpressionFactory factory;

    public OracleTransformer(IConfig config)
    {
        //this.config = config;
        factory = config.getSqlExpressionFactory();
    }

    @Override
    public SqlExpressionFactory getSqlExpressionFactory()
    {
        return factory;
    }

    @Override
    public ISqlExpression typeCast(ISqlExpression value, Class<?> type)
    {
        List<String> templates = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>();
        if (isBool(type)) {
            templates.add("CAST(");
            sqlExpressions.add(value);
            templates.add(" AS BOOLEAN)");
        }
        else if (isDate(type) || isDateTime(type)) {
            templates.add("CAST(");
            sqlExpressions.add(value);
            templates.add(" AS DATE)");
        }
        else if (isByte(type) || isShort(type) || isInt(type) || isLong(type) || isFloat(type) || isDouble(type) || isDecimal(type)) {
            templates.add("CAST(");
            sqlExpressions.add(value);
            templates.add(" AS NUMBER)");
        }
        else if (isString(type)) {
            templates.add("TO_CHAR(");
            sqlExpressions.add(value);
            templates.add(")");
        }
        else if (isChar(type)) {
            templates.add("SUBSTR(TO_CHAR(");
            sqlExpressions.add(value);
            templates.add("),1,1)");
        }
        else {
            throw new UnsupportedOperationException("不支持的Java类型:" + type.getName());
        }
        return factory.template(templates, sqlExpressions);
    }

    public ISqlExpression If(ISqlExpression cond, ISqlExpression truePart, ISqlExpression falsePart) {
        return factory.template(Arrays.asList("(CASE WHEN ", " THEN ", " ELSE ", " END)"), Arrays.asList(cond, truePart, falsePart));
    }

    public ISqlTemplateExpression groupConcat(List<ISqlExpression> expressions) {
        List<String> strings;
        List<ISqlExpression> args;
        //无分隔符
        if (expressions.size() == 1) {
            ISqlExpression property = expressions.get(0);
            strings = Arrays.asList("LISTAGG(", ") WITHIN GROUP (ORDER BY ", ")");
            args = Arrays.asList(property, property);
        }
        //有分隔符
        else {
            ISqlExpression delimiter = expressions.get(0);
            ISqlExpression property = expressions.get(1);
            strings = Arrays.asList("LISTAGG(", ",", ") WITHIN GROUP (ORDER BY ", ")");
            args = Arrays.asList(property, delimiter, property);
        }
        return factory.template(strings, args);
    }

    public ISqlTemplateExpression toDegrees(ISqlExpression arg) {
        return factory.template(Arrays.asList("(", " * 180 / " + Math.PI + ")"), Collections.singletonList(arg));
    }

    public ISqlTemplateExpression toRadians(ISqlExpression arg) {
        return factory.template(Arrays.asList("(", " * " + Math.PI + " / 180)"), Collections.singletonList(arg));
    }

    public ISqlTemplateExpression log10(ISqlExpression arg) {
        return factory.template(Arrays.asList("LOG(10,", ")"), Collections.singletonList(arg));
    }

    public ISqlTemplateExpression random() {
        return factory.template(Collections.singletonList("DBMS_RANDOM.VALUE"), Collections.emptyList());
    }

    public ISqlExpression remainder(ISqlExpression left, ISqlExpression right) {
        return factory.template(Arrays.asList("MOD(", ",", ")"), Arrays.asList(left, right));
    }

    public ISqlBinaryExpression contains(ISqlExpression left, ISqlExpression right) {
        ISqlTemplateExpression function = factory.template(Arrays.asList("('%'||", "||'%')"), Collections.singletonList(right));
        return factory.binary(SqlOperator.LIKE, left, function);
    }

    public ISqlTemplateExpression length(ISqlExpression thiz) {
        return factory.template(Arrays.asList("NVL(LENGTH(", "),0)"), Collections.singletonList(thiz));
    }

    public ISqlTemplateExpression indexOf(ISqlExpression thisStr, ISqlExpression subStr, ISqlExpression fromIndex) {
        return factory.template(Arrays.asList("INSTR(", ",", ",", ")"), Arrays.asList(thisStr, subStr, fromIndex));
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
