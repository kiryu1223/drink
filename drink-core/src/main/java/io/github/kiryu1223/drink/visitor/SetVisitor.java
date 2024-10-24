package io.github.kiryu1223.drink.visitor;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlSetExpression;
import io.github.kiryu1223.drink.base.expression.ISqlSetsExpression;
import io.github.kiryu1223.expressionTree.expressions.BlockExpression;
import io.github.kiryu1223.expressionTree.expressions.Expression;

import java.util.ArrayList;
import java.util.List;

public class SetVisitor extends SqlVisitor
{
    public SetVisitor(IConfig config)
    {
        super(config);
    }

    public SetVisitor(IConfig config, int offset)
    {
        super(config, offset);
    }

    @Override
    public ISqlExpression visit(BlockExpression blockExpression)
    {
        List<ISqlSetExpression> sqlSetExpressions=new ArrayList<>();
        for (Expression expression : blockExpression.getExpressions())
        {
            ISqlExpression visit = visit(expression);
            if(visit instanceof ISqlSetExpression)
            {
                sqlSetExpressions.add((ISqlSetExpression) visit);
            }
        }
        ISqlSetsExpression sets = factory.sets();
        sets.addSet(sqlSetExpressions);
        return sets;
    }

    @Override
    protected SqlVisitor getSelf()
    {
        return new SetVisitor(config);
    }
}
