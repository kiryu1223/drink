/*
 * Copyright 2017-2024 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.kiryu1223.drink.base.transform.method.impl;


import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.transform.method.IStringMethods;
import io.github.kiryu1223.drink.base.transform.Transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringMethods extends Transform implements IStringMethods {

    public StringMethods(IConfig config) {
        super(config);
    }

    public ISqlBinaryExpression contains(ISqlExpression left, ISqlExpression right) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        switch (config.getDbType()) {
            case Oracle:
            case SQLite:
                functions = Arrays.asList("('%'||", "||'%')");
                break;
            default:
                functions = Arrays.asList("CONCAT('%',", ",'%')");
                break;
        }
        ISqlTemplateExpression function = factory.template(functions, Collections.singletonList(right));
        return factory.binary(SqlOperator.LIKE, left, function);
    }

    public ISqlBinaryExpression startsWith(ISqlExpression left, ISqlExpression right) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        List<ISqlExpression> args = Collections.singletonList(right);
        switch (config.getDbType()) {
            case SQLite:
                functions = Arrays.asList("(", "||'%')");
                break;
            default:
                functions = Arrays.asList("CONCAT(", ",'%')");
                break;
        }
        return factory.binary(SqlOperator.LIKE, left, factory.template(functions, args));
    }

    public ISqlBinaryExpression endsWith(ISqlExpression left, ISqlExpression right) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        List<ISqlExpression> args = Collections.singletonList(right);
        switch (config.getDbType()) {
            case SQLite:
                functions = Arrays.asList("('%'||", ")");
                break;
            default:
                functions = Arrays.asList("CONCAT('%',", ")");
                break;
        }
        return factory.binary(SqlOperator.LIKE, left, factory.template(functions, args));
    }

    public ISqlTemplateExpression length(ISqlExpression thiz) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        switch (config.getDbType()) {
            case SQLServer:
                functions = Arrays.asList("LEN(", ")");
                break;
            case Oracle:
                functions = Arrays.asList("NVL(LENGTH(", "),0)");
                break;
            case SQLite:
            case PostgreSQL:
                functions = Arrays.asList("LENGTH(", ")");
                break;
            case MySQL:
            case H2:
            default:
                functions = Arrays.asList("CHAR_LENGTH(", ")");
        }
        return factory.template(functions, Collections.singletonList(thiz));
    }

    public ISqlTemplateExpression toUpperCase(ISqlExpression thiz) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions = Arrays.asList("UPPER(", ")");
        return factory.template(functions, Collections.singletonList(thiz));
    }

    public ISqlTemplateExpression toLowerCase(ISqlExpression thiz) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions = Arrays.asList("LOWER(", ")");
        return factory.template(functions, Collections.singletonList(thiz));
    }

    public ISqlTemplateExpression concat(ISqlExpression left, ISqlExpression right) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        switch (config.getDbType()) {
            case SQLite:
                functions = Arrays.asList("(", "||", ")");
                break;
            default:
                functions = Arrays.asList("CONCAT(", ",", ")");
        }
        return factory.template(functions, Arrays.asList(left, right));
    }

    public ISqlTemplateExpression trim(ISqlExpression thiz) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions = Arrays.asList("TRIM(", ")");
        return factory.template(functions, Collections.singletonList(thiz));
    }

    public ISqlExpression isEmpty(ISqlExpression thiz) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        switch (config.getDbType()) {
            case SQLServer:
                return factory.parens(factory.binary(SqlOperator.EQ, factory.template(Arrays.asList("DATALENGTH(", ")"), Collections.singletonList(thiz)), factory.constString("0")));
            default:
                return factory.parens(factory.binary(SqlOperator.EQ, length(thiz), factory.constString("0")));
        }
//        if (config.getDbType() == DbType.SQLServer)
//        {
//            return factory.template(Arrays.asList("IIF(", ",1,0)"), Collections.singletonList(parens));
//        }
//        else
//        {
//            return parens;
//        }
    }

    public ISqlTemplateExpression indexOf(ISqlExpression thisStr, ISqlExpression subStr) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        List<ISqlExpression> sqlExpressions;
        switch (config.getDbType()) {
            case SQLServer:
                functions = Arrays.asList("CHARINDEX(", ",", ")");
                sqlExpressions = Arrays.asList(subStr, thisStr);
                break;
            case PostgreSQL:
                functions = Arrays.asList("STRPOS(", ",", ")");
                sqlExpressions = Arrays.asList(thisStr, subStr);
                break;
            default:
                functions = Arrays.asList("INSTR(", ",", ")");
                sqlExpressions = Arrays.asList(thisStr, subStr);
        }
        return factory.template(functions, sqlExpressions);
    }

    public ISqlTemplateExpression indexOf(ISqlExpression thisStr, ISqlExpression subStr, ISqlExpression fromIndex) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        List<ISqlExpression> sqlExpressions;
        switch (config.getDbType()) {
            case SQLServer:
                functions = Arrays.asList("CHARINDEX(", ",", ",", ")");
                sqlExpressions = Arrays.asList(subStr, thisStr, fromIndex);
                break;
            case Oracle:
                functions = Arrays.asList("INSTR(", ",", ",", ")");
                sqlExpressions = Arrays.asList(thisStr, subStr, fromIndex);
                break;
            case SQLite:
                functions = Arrays.asList("(INSTR(SUBSTR(", ",", " + 1),", ") + ", ")");
                sqlExpressions = Arrays.asList(thisStr, fromIndex, subStr, fromIndex);
                break;
            case PostgreSQL:
                functions = Arrays.asList("(STRPOS(SUBSTR(", ",", " + 1),", ") + ", ")");
                sqlExpressions = Arrays.asList(thisStr, fromIndex, subStr, fromIndex);
                break;
            default:
                functions = Arrays.asList("LOCATE(", ",", ",", ")");
                sqlExpressions = Arrays.asList(subStr, thisStr, fromIndex);
        }
        return factory.template(functions, sqlExpressions);
    }

    public ISqlTemplateExpression replace(ISqlExpression thisStr, ISqlExpression oldStr, ISqlExpression newStr) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions = Arrays.asList("REPLACE(", ",", ",", ")");
        List<ISqlExpression> sqlExpressions = Arrays.asList(thisStr, oldStr, newStr);
        return factory.template(functions, sqlExpressions);
    }

    public ISqlTemplateExpression substring(ISqlExpression thisStr, ISqlExpression beginIndex) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        List<ISqlExpression> sqlExpressions;
        switch (config.getDbType()) {
            case SQLServer:
                functions = Arrays.asList("SUBSTRING(", ",", ",LEN(", ") - (", " - 1))");
                sqlExpressions = Arrays.asList(thisStr, beginIndex, thisStr, beginIndex);
                break;
            default:
                functions = Arrays.asList("SUBSTR(", ",", ")");
                sqlExpressions = Arrays.asList(thisStr, beginIndex);
                break;
        }
        return factory.template(functions, sqlExpressions);
    }

    public ISqlTemplateExpression substring(ISqlExpression thisStr, ISqlExpression beginIndex, ISqlExpression endIndex) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        List<ISqlExpression> sqlExpressions = Arrays.asList(thisStr, beginIndex, endIndex);
        switch (config.getDbType()) {
            case SQLServer:
                functions = Arrays.asList("SUBSTRING(", ",", ",", ")");
                break;
            default:
                functions = Arrays.asList("SUBSTR(", ",", ",", ")");
        }
        return factory.template(functions, sqlExpressions);
    }

    public ISqlTemplateExpression joinArray(ISqlExpression delimiter, List<ISqlExpression> elements) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>(1 + elements.size());
        switch (config.getDbType()) {
            case Oracle:
            case SQLite:
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
                break;
            default:
                sqlExpressions.add(delimiter);
                sqlExpressions.addAll(elements);
                functions.add("CONCAT_WS(");
                functions.add(",");
                for (int i = 0; i < sqlExpressions.size(); i++) {
                    if (i < elements.size() - 1) functions.add(",");
                }
                functions.add(")");
        }
        return factory.template(functions, sqlExpressions);
    }

    public ISqlTemplateExpression joinList(ISqlExpression delimiter, ISqlExpression elements) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        List<ISqlExpression> sqlExpressions;
        if (elements instanceof ISqlCollectedValueExpression) {
            if (config.getDbType() == DbType.Oracle || config.getDbType() == DbType.SQLite) {
                ISqlCollectedValueExpression expression = (ISqlCollectedValueExpression) elements;
                List<Object> collection = new ArrayList<>(expression.getCollection());
                functions = new ArrayList<>(collection.size() * 2);
                sqlExpressions = new ArrayList<>(collection.size() * 2);
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
            }
            else {
                functions = new ArrayList<>();
                sqlExpressions = Arrays.asList(delimiter, elements);
            }
        }
        else {
            throw new RuntimeException("String.join()的第二个参数必须是java中能获取到的");
        }
        switch (config.getDbType()) {
            case Oracle:
            case SQLite:
                break;
            default:
                functions.add("CONCAT_WS(");
                functions.add(",");
                functions.add(")");
        }
        return factory.template(functions, sqlExpressions);
    }
}
