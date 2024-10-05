package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.exception.IllegalExpressionException;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.util.LinkedHashMap;


public class GroupByVisitor extends SqlVisitor
{
    public GroupByVisitor(Config config)
    {
        super(config);
    }

    @Override
    public SqlExpression visit(NewExpression newExpression)
    {
        BlockExpression classBody = newExpression.getClassBody();
        if (classBody == null)
        {
            return super.visit(newExpression);
        }
        else
        {
            LinkedHashMap<String, SqlExpression> contextMap = new LinkedHashMap<>();
            for (Expression expression : classBody.getExpressions())
            {
                if (expression.getKind() == Kind.Variable)
                {
                    VariableExpression variableExpression = (VariableExpression) expression;
                    String name = variableExpression.getName();
                    SqlExpression sqlExpression = visit(variableExpression.getInit());
                    contextMap.put(name, sqlExpression);
                }
            }
            return factory.groupBy(contextMap);
        }
    }

    @Override
    protected GroupByVisitor getSelf()
    {
        return new GroupByVisitor(config);
    }
}
