package io.github.kiryu1223.drink.base.transform.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.expression.SqlOperator;
import io.github.kiryu1223.drink.base.transform.Transform;
import io.github.kiryu1223.drink.base.transform.expression.ILogic;

import java.util.Arrays;
import java.util.List;

public class Logic extends Transform implements ILogic {
    public Logic(IConfig config) {
        super(config);
    }

    /**
     * if表达式
     */
    public ISqlExpression If(ISqlExpression cond, ISqlExpression truePart, ISqlExpression falsePart) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> function;
        List<ISqlExpression> args = Arrays.asList(cond, truePart, falsePart);
        switch (config.getDbType()) {
            case SQLServer:
            case SQLite:
                function = Arrays.asList("IIF(", ",", ",", ")");
                break;
            case Oracle:
            case PostgreSQL:
                function = Arrays.asList("(CASE WHEN ", " THEN ", " ELSE ", " END)");
                break;
            default:
                function = Arrays.asList("IF(", ",", ",", ")");
        }
        return factory.template(function, args);
    }

    @Override
    public ISqlExpression typeCast(ISqlExpression value, ISqlExpression type) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.template(Arrays.asList("CAST(", ",", ")"), Arrays.asList(value, type));
    }
}
