package io.github.kiryu1223.drink.ext;

import io.github.kiryu1223.drink.core.expression.SqlExpression;

import java.util.ArrayList;
import java.util.List;

public class FunctionBox
{
    private final List<String> functions = new ArrayList<>();
    private final List<SqlExpression> sqlExpressions = new ArrayList<>();

    public List<String> getFunctions()
    {
        return functions;
    }

    public List<SqlExpression> getSqlExpressions()
    {
        return sqlExpressions;
    }
}
