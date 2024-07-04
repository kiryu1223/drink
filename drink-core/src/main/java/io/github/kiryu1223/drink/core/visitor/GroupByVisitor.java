package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.context.SqlGroupContext;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.util.LinkedHashMap;


public class GroupByVisitor extends SqlVisitor
{
    public GroupByVisitor(Config config)
    {
        super(config);
    }

    @Override
    public SqlContext visit(NewExpression newExpression)
    {
        BlockExpression classBody = newExpression.getClassBody();
        if (classBody == null) throw new RuntimeException();
        LinkedHashMap<String, SqlContext> contextMap = new LinkedHashMap<>();
        for (Expression expression : classBody.getExpressions())
        {
            if (expression.getKind() == Kind.Variable)
            {
                VariableExpression variableExpression = (VariableExpression) expression;
                String name = variableExpression.getName();
                SqlContext context = visit(variableExpression.getInit());
                contextMap.put(name, context);
            }
        }
        return new SqlGroupContext(contextMap);
    }

    @Override
    protected GroupByVisitor getSelf()
    {
        return new GroupByVisitor(config);
    }
}
