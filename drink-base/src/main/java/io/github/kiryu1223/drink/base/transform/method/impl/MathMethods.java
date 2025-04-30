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


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTemplateExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.transform.method.IMathMethods;
import io.github.kiryu1223.drink.base.transform.Transform;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 数学运算函数
 *
 * @author kiryu1223
 * @since 3.0
 */
public class MathMethods extends Transform implements IMathMethods {
    public MathMethods(IConfig config) {
        super(config);
    }

    /**
     * 数据库atan2函数
     */
    public ISqlTemplateExpression atan2(ISqlExpression arg1, ISqlExpression arg2) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        switch (config.getDbType()) {
            case SQLServer:
                function = Arrays.asList("ATN2(", ",", ")");
                break;
            default:
                function = Arrays.asList("ATAN2(", ",", ")");
        }
        return factory.template(function, Arrays.asList(arg1, arg2));
    }

    /**
     * 数据库ceil函数
     */
    public ISqlTemplateExpression ceil(ISqlExpression arg) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        switch (config.getDbType()) {
            case SQLServer:
                function = Arrays.asList("CEILING(", ")");
                break;
            default:
                function = Arrays.asList("CEIL(", ")");
        }
        return factory.template(function, Collections.singletonList(arg));
    }

    /**
     * 数据库degrees函数
     */
    public ISqlTemplateExpression toDegrees(ISqlExpression arg) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        switch (config.getDbType()) {
            case Oracle:
                function = Arrays.asList("(", " * 180 / " + Math.PI + ")");
                break;
            default:
                function = Arrays.asList("DEGREES(", ")");
        }
        return factory.template(function, Collections.singletonList(arg));
    }

    /**
     * 数据库radians函数
     */
    public ISqlTemplateExpression toRadians(ISqlExpression arg) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        switch (config.getDbType()) {
            case Oracle:
                function = Arrays.asList("(", " * " + Math.PI + " / 180)");
                break;
            default:
                function = Arrays.asList("RADIANS(", ")");
        }
        return factory.template(function, Collections.singletonList(arg));
    }

    /**
     * 数据库log函数
     */
    public ISqlTemplateExpression log(ISqlExpression arg) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        switch (config.getDbType()) {
            case SQLServer:
                function = Arrays.asList("LOG(", ")");
                break;
            default:
                function = Arrays.asList("LN(", ")");
        }
        return factory.template(function, Collections.singletonList(arg));
    }

    /**
     * 数据库log10函数
     */
    public ISqlTemplateExpression log10(ISqlExpression arg) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        switch (config.getDbType()) {
            case SQLServer:
                function = Arrays.asList("LOG(", ",10)");
                break;
            case Oracle:
                function = Arrays.asList("LOG(10,", ")");
                break;
            default:
                function = Arrays.asList("LOG10(", ")");
        }
        return factory.template(function, Collections.singletonList(arg));
    }

    /**
     * 数据库random函数
     */
    public ISqlTemplateExpression random(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        switch (config.getDbType()) {
            case Oracle:
                function = Collections.singletonList("DBMS_RANDOM.VALUE");
                break;
            case SQLite:
                function = Collections.singletonList("ABS(RANDOM() / 10000000000000000000.0)");
                break;
            case PostgreSQL:
                function = Collections.singletonList("RANDOM()");
                break;
            default:
                function = Collections.singletonList("RAND()");
        }
        return factory.template(function, Collections.emptyList());
    }

    /**
     * 数据库round函数
     */
    public ISqlTemplateExpression round(ISqlExpression arg) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        switch (config.getDbType()) {
            case SQLServer:
                function = Arrays.asList("ROUND(", ",0)");
                break;
            default:
                function = Arrays.asList("ROUND(", ")");
        }
        return factory.template(function, Collections.singletonList(arg));
    }
}
