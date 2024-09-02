package io.github.kiryu1223.drink.core.visitor.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlGroupByExpression;
import io.github.kiryu1223.expressionTree.expressions.FieldSelectExpression;

import java.util.Map;

import static io.github.kiryu1223.drink.core.visitor.expression.ExpressionUtil.isGroupKey;

public class HavingVisitor extends SqlVisitor
{
    private final SqlGroupByExpression group;

    public HavingVisitor(SqlGroupByExpression group, Config config)
    {
        super(config);
        this.group = group;
    }

    @Override
    public SqlExpression visit(FieldSelectExpression fieldSelect)
    {
        if (isGroupKey(parameters, fieldSelect)) // g.key
        {
            return group;
        }
        else if (isGroupKey(parameters, fieldSelect.getExpr())) // g.key.xxx
        {
            Map<String, SqlExpression> columns = group.getColumns();
            return columns.get(fieldSelect.getField().getName());
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
