package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlSetExpression;
import io.github.kiryu1223.drink.core.expression.SqlSetsExpression;
import io.github.kiryu1223.expressionTree.expressions.BlockExpression;
import io.github.kiryu1223.expressionTree.expressions.Expression;

import java.util.ArrayList;
import java.util.List;

public class SetVisitor extends SqlVisitor
{
    public SetVisitor(Config config)
    {
        super(config);
    }

    public SetVisitor(Config config, int offset)
    {
        super(config, offset);
    }

    @Override
    public SqlExpression visit(BlockExpression blockExpression)
    {
        List<SqlSetExpression> sqlSetExpressions=new ArrayList<>();
        for (Expression expression : blockExpression.getExpressions())
        {
            SqlExpression visit = visit(expression);
            if(visit instanceof SqlSetExpression)
            {
                sqlSetExpressions.add((SqlSetExpression) visit);
            }
        }
        SqlSetsExpression sets = factory.sets();
        sets.addSet(sqlSetExpressions);
        return sets;
    }

    @Override
    protected SqlVisitor getSelf()
    {
        return new SetVisitor(config);
    }
}
