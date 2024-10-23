package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.nnnn.expression.SqlGroupByExpression;
import io.github.kiryu1223.drink.nnnn.expression.SqlQueryableExpression;
import io.github.kiryu1223.expressionTree.expressions.FieldSelectExpression;

import java.util.Map;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.isGroupKey;

public class HavingVisitor extends SqlVisitor
{
    private final SqlQueryableExpression queryable;

    public HavingVisitor(Config config, SqlQueryableExpression queryable)
    {
        super(config);
        this.queryable = queryable;
    }

    @Override
    public SqlExpression visit(FieldSelectExpression fieldSelect)
    {
        if (isGroupKey(parameters, fieldSelect)) // g.key
        {
            SqlGroupByExpression groupBy = queryable.getGroupBy();
            return groupBy.getColumns().get("key");
        }
        else if (isGroupKey(parameters, fieldSelect.getExpr())) // g.key.xxx
        {
            Map<String, SqlExpression> columns = queryable.getGroupBy().getColumns();
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
        return new HavingVisitor(config, queryable);
    }
}
