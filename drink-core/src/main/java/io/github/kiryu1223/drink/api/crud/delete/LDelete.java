package io.github.kiryu1223.drink.api.crud.delete;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.visitor.NormalVisitor;
import io.github.kiryu1223.drink.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.delegate.Func2;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LDelete<T> extends DeleteBase
{
    public LDelete(Config config, Class<T> c)
    {
        super(config);
        getSqlBuilder().setFromTable(c);
    }

    //region [JOIN]
    public <Tn> LDelete2<T, Tn> innerJoin(Class<Tn> target, @Expr Func2<T, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LDelete2<T, Tn> innerJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return new LDelete2<>(getSqlBuilder());
    }

    public <Tn> LDelete2<T, Tn> leftJoin(Class<Tn> target, @Expr Func2<T, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LDelete2<T, Tn> leftJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return new LDelete2<>(getSqlBuilder());
    }

    public <Tn> LDelete2<T, Tn> rightJoin(Class<Tn> target, @Expr Func2<T, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LDelete2<T, Tn> rightJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return new LDelete2<>(getSqlBuilder());
    }
    //endregion

    // region [WHERE]
    public LDelete<T> where(@Expr Func1<T, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LDelete<T> where(ExprTree<Func1<T, Boolean>> expr)
    {
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlContext context = normalVisitor.visit(expr.getTree());
        getSqlBuilder().addWhere(context);
        return this;
    }

    // endregion
}
