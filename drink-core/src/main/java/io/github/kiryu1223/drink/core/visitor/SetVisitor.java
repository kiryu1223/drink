package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.context.SqlSetContext;
import io.github.kiryu1223.drink.core.context.SqlSetsContext;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.util.ArrayList;
import java.util.List;

public class SetVisitor extends SqlVisitor
{
    public SetVisitor(Config config)
    {
        super(config);
    }

    @Override
    public SqlContext visit(BlockExpression blockExpression)
    {
        List<SqlSetContext> setContexts = new ArrayList<>();
        for (Expression expression : blockExpression.getExpressions())
        {
            SqlContext context = visit(expression);
            if(context instanceof SqlSetContext)
            {
                setContexts.add((SqlSetContext) context);
            }
//            if (expression instanceof MethodCallExpression)
//            {
//                MethodCallExpression methodCall = (MethodCallExpression) expression;
//                Expression expr = methodCall.getExpr();
//                if (expr instanceof ParameterExpression && parameters.contains(expr))
//                {
//                    Method method = methodCall.getMethod();
//                    if (isSetter(method))
//                    {
//                        int index = parameters.indexOf(expr);
//                        MetaData metaData = MetaDataCache.getMetaData(method.getDeclaringClass());
//                        String name = metaData.getColumnNameBySetter(method);
//                        SqlContext context = visit(methodCall.getArgs().get(0));
//                        SqlPropertyContext sqlPropertyContext = new SqlPropertyContext(name, index);
//                        SqlSetContext sqlSetContext = new SqlSetContext(sqlPropertyContext, context);
//                        setContexts.add(sqlSetContext);
//                    }
//                }
//            }
//            else if (expression instanceof AssignExpression)
//            {
//                AssignExpression assignExpression = (AssignExpression) expression;
//                SqlContext left = visit(assignExpression.getLeft());
//                if (left instanceof SqlPropertyContext)
//                {
//                    SqlPropertyContext sqlPropertyContext = (SqlPropertyContext) left;
//                    SqlContext right = visit(assignExpression.getRight());
//                    SqlSetContext sqlSetContext = new SqlSetContext(sqlPropertyContext, right);
//                    setContexts.add(sqlSetContext);
//                }
//            }
        }
        return new SqlSetsContext(setContexts);
    }

    @Override
    protected SqlVisitor getSelf()
    {
        return new SetVisitor(config);
    }
}
