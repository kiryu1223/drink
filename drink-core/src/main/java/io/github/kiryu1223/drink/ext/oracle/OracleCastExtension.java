package io.github.kiryu1223.drink.ext.oracle;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlTypeExpression;
import io.github.kiryu1223.drink.ext.BaseSqlExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class OracleCastExtension extends BaseSqlExtension
{
//    static
//    {
//        new OracleCastExtension();
//    }

    @Override
    public SqlExpression parse(Config config, Method sqlFunc, List<SqlExpression> args)
    {
        List<String> templates = new ArrayList<>();
        List<SqlExpression> sqlExpressions = new ArrayList<>();
        SqlExpression expression = args.get(1);
        if (expression instanceof SqlTypeExpression)
        {
            SqlTypeExpression typeExpression = (SqlTypeExpression) expression;
            Class<?> type = typeExpression.getType();
            if (type == String.class)
            {
                templates.add("TO_CHAR(");
                templates.add(")");
                sqlExpressions.add(args.get(0));
                return config.getSqlExpressionFactory().template(templates, sqlExpressions);
            }
            else if (type == char.class || type == Character.class)
            {
                templates.add("SUBSTR(TO_CHAR(");
                templates.add("),1,1)");
                sqlExpressions.add(args.get(0));
                return config.getSqlExpressionFactory().template(templates, sqlExpressions);
            }
        }
        templates.add("CAST(");
        templates.add(" AS ");
        templates.add(")");
        sqlExpressions.add(args.get(0));
        sqlExpressions.add(args.get(1));
        return config.getSqlExpressionFactory().template(templates, sqlExpressions);
    }
}
