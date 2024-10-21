package io.github.kiryu1223.drink.ext.sqlite;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.ext.BaseSqlExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SqliteJoinExtension extends BaseSqlExtension
{
    @Override
    public SqlExpression parse(Config config, Method sqlFunc, List<SqlExpression> args)
    {
        List<String> templates = new ArrayList<>();
        List<SqlExpression> sqlExpressions = new ArrayList<>();
        SqlExpression separator = args.get(0);
        templates.add("(");
        for (int i = 1; i < args.size(); i++)
        {
            sqlExpressions.add(args.get(i));
            if (i < args.size() - 1)
            {
                templates.add("||");
                sqlExpressions.add(separator);
                templates.add("||");
            }
        }
        templates.add(")");
        return config.getSqlExpressionFactory().template(templates, sqlExpressions);
    }
}
