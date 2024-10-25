package io.github.kiryu1223.drink.core.sqlExt.sqlite;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.sqlExt.BaseSqlExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SqliteJoinExtension extends BaseSqlExtension
{
    @Override
    public ISqlExpression parse(IConfig config, Method sqlFunc, List<ISqlExpression> args)
    {
        List<String> templates = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>();
        ISqlExpression separator = args.get(0);
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
