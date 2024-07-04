package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.context.SqlGroupContext;
import io.github.kiryu1223.expressionTree.expressions.FieldSelectExpression;

import java.util.Map;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.isGroupKey;

public class HavingVisitor extends SqlVisitor
{
    private final SqlContext group;

    public HavingVisitor(SqlContext group, Config config)
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
    protected SqlVisitor getSelf()
    {
        return new HavingVisitor(group, config);
    }
}
