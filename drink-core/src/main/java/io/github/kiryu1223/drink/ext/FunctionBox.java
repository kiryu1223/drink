package io.github.kiryu1223.drink.ext;

import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.expressionTree.expressions.Expression;

import java.util.ArrayList;
import java.util.List;

public class FunctionBox
{
    private final List<String> functions = new ArrayList<>();
    private final List<SqlContext> sqlContexts = new ArrayList<>();

    public List<String> getFunctions()
    {
        return functions;
    }

    public List<SqlContext> getSqlContexts()
    {
        return sqlContexts;
    }
}
