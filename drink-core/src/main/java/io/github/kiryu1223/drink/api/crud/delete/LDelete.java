package io.github.kiryu1223.drink.api.crud.delete;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.visitor.WhereVisitor;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LDelete<T> extends DeleteBase
{
    public LDelete(Config config, Class<T> c)
    {
        super(config);
        getSqlBuilder().setDeleteTable(c);
    }

    public LDelete<T> where(@Expr Func1<T, Boolean> func)
    {
        throw new RuntimeException();
    }

    public LDelete<T> where(ExprTree<Func1<T, Boolean>> expr)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext context = whereVisitor.visit(expr.getTree());
        getSqlBuilder().addWhere(context);
        return this;
    }
}
