package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.api.crud.read.group.IAggregation;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.context.SqlFuncContext;
import io.github.kiryu1223.drink.core.context.SqlGroupContext;
import io.github.kiryu1223.expressionTree.expressions.Expression;
import io.github.kiryu1223.expressionTree.expressions.FieldSelectExpression;
import io.github.kiryu1223.expressionTree.expressions.MethodCallExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.isGroupKey;

public class HavingVisitor extends SqlVisitor
{
    private final SqlContext group;

    public HavingVisitor( SqlContext group,Config config)
    {
        super(config);
        this.group = group;
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
    public SqlContext visit(MethodCallExpression methodCall)
    {
        if (IAggregation.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            String name = methodCall.getMethod().getName();
            List<SqlContext> args = new ArrayList<>(methodCall.getArgs().size());
            for (Expression arg : methodCall.getArgs())
            {
                args.add(visit(arg));
            }
            return new SqlFuncContext(name, args);
        }
        else
        {
            return super.visit(methodCall);
        }
    }

    @Override
    protected SqlVisitor getSelf()
    {
        return new HavingVisitor(group, config);
    }
}
