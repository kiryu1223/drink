package io.github.kiryu1223.drink.api.crud.update;


import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.visitor.SetVisitor;
import io.github.kiryu1223.drink.core.visitor.WhereVisitor;
import io.github.kiryu1223.drink.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Action1;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.delegate.Func2;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LUpdate<T> extends UpdateBase
{
    // region [INIT]
    public LUpdate(Config config, Class<T> t)
    {
        super(config);
        getSqlBuilder().setMainTable(t);
    }
    // endregion

    //region [JOIN]
    public <Tn> LUpdate2<T, Tn> innerJoin(Class<Tn> target, @Expr Func2<T, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LUpdate2<T, Tn> innerJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return new LUpdate2<>(getSqlBuilder());
    }

    public <Tn> LUpdate2<T, Tn> leftJoin(Class<Tn> target, @Expr Func2<T, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LUpdate2<T, Tn> leftJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return new LUpdate2<>(getSqlBuilder());
    }

    public <Tn> LUpdate2<T, Tn> rightJoin(Class<Tn> target, @Expr Func2<T, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LUpdate2<T, Tn> rightJoin(Class<Tn> target, ExprTree<Func2<T, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return new LUpdate2<>(getSqlBuilder());
    }
    //endregion

    //region [SET]
    public LUpdate<T> set(@Expr Action1<T> action)
    {
        throw new NotCompiledException();
    }

    public LUpdate<T> set(ExprTree<Action1<T>> expr)
    {
        SetVisitor setVisitor = new SetVisitor(getConfig());
        SqlContext context = setVisitor.visit(expr.getTree());
        getSqlBuilder().addSet(context);
        return this;
    }
    //endregion

    //region [WHERE]

    public LUpdate<T> where(@Expr Func1<T, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LUpdate<T> where(ExprTree<Func1<T, Boolean>> expr)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext context = whereVisitor.visit(expr.getTree());
        getSqlBuilder().addWhere(context);
        return this;
    }

    //endregion
}
