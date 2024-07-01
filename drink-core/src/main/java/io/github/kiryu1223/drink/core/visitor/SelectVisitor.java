package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.core.context.*;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.*;
import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.isGroupKey;

public class SelectVisitor extends SqlVisitor
{
    private ParameterExpression cur;
    private final SqlContext group;

    public SelectVisitor(SqlContext group)
    {
        this.group = group;
    }

    @Override
    public SqlContext visit(NewExpression newExpression)
    {
        BlockExpression classBody = newExpression.getClassBody();
        if (classBody == null) throw new RuntimeException();
        List<SqlContext> contexts = new ArrayList<>();
        for (Expression expression : classBody.getExpressions())
        {
            if (expression.getKind() == Kind.Variable)
            {
                VariableExpression variableExpression = (VariableExpression) expression;
                String name = variableExpression.getName();
                SqlContext context = visit(variableExpression.getInit());
                setAs(contexts, context, name);
            }
        }
        return new SqlSelectorContext(contexts);
    }

    @Override
    public SqlContext visit(BlockExpression blockExpression)
    {
        List<SqlContext> contexts = new ArrayList<>();
        for (Expression expression : blockExpression.getExpressions())
        {
            if (expression instanceof VariableExpression)
            {
                VariableExpression variableExpression = (VariableExpression) expression;
                if (cur == null)
                {
                    cur = variableExpression.getParameter();
                }
                else
                {
                    throw new RuntimeException();
                }
            }
            else if (expression instanceof MethodCallExpression)
            {
                MethodCallExpression methodCallExpression = (MethodCallExpression) expression;
                Method method = methodCallExpression.getMethod();
                if (isSetter(method) && methodCallExpression.getExpr() == cur)
                {
                    String name = propertyName(method);
                    SqlContext context = visit(methodCallExpression.getArgs().get(0));
                    setAs(contexts, context, name);
                }
            }
        }
        return new SqlSelectorContext(contexts);
    }

    @Override
    public SqlContext visit(FieldSelectExpression fieldSelect)
    {
        if (isGroupKey(parameters, fieldSelect)) // g.key
        {
            return group;
        }
        else if (isGroupKey(parameters, fieldSelect.getExpr())) // g.key.xxx
        {
            SqlGroupContext groupContext = (SqlGroupContext) group;
            Map<String, SqlContext> contextMap = groupContext.getContextMap();
            return groupContext.getContextMap().get(fieldSelect.getField().getName());
        }
        else
        {
            return super.visit(fieldSelect);
        }
    }

    @Override
    protected SqlVisitor getSelf()
    {
        return new SelectVisitor(group);
    }

    private void setAs(List<SqlContext> contexts, SqlContext context, String name)
    {
        if (context instanceof SqlPropertyContext)
        {
            SqlPropertyContext propertyContext = (SqlPropertyContext) context;
            if (!propertyContext.getProperty().equals(name))
            {
                contexts.add(new SqlAsNameContext(name, context));
            }
            else
            {
                contexts.add(context);
            }
        }
        else
        {
            contexts.add(new SqlAsNameContext(name, context));
        }
    }
}
