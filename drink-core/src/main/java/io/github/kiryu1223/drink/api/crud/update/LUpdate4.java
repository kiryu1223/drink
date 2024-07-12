package io.github.kiryu1223.drink.api.crud.update;

import io.github.kiryu1223.drink.api.crud.builder.UpdateSqlBuilder;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.visitor.SetVisitor;
import io.github.kiryu1223.drink.core.visitor.WhereVisitor;
import io.github.kiryu1223.expressionTree.delegate.Action4;
import io.github.kiryu1223.expressionTree.delegate.Func4;
import io.github.kiryu1223.expressionTree.delegate.Func5;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LUpdate4<T1, T2, T3, T4> extends UpdateBase
{
    public LUpdate4(UpdateSqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    //region [JOIN]
    public <Tn> LUpdate5<T1, T2, T3, T4, Tn> innerJoin(Class<Tn> target, @Expr Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LUpdate5<T1, T2, T3, T4, Tn> innerJoin(Class<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return new LUpdate5<>(getSqlBuilder());
    }

    public <Tn> LUpdate5<T1, T2, T3, T4, Tn> leftJoin(Class<Tn> target, @Expr Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LUpdate5<T1, T2, T3, T4, Tn> leftJoin(Class<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return new LUpdate5<>(getSqlBuilder());
    }

    public <Tn> LUpdate5<T1, T2, T3, T4, Tn> rightJoin(Class<Tn> target, @Expr Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LUpdate5<T1, T2, T3, T4, Tn> rightJoin(Class<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return new LUpdate5<>(getSqlBuilder());
    }
    //endregion

    //region [SET]
    public LUpdate4<T1, T2, T3, T4> set(@Expr Action4<T1, T2, T3, T4> action)
    {
        throw new RuntimeException();
    }

    public LUpdate4<T1, T2, T3, T4> set(ExprTree<Action4<T1, T2, T3, T4>> expr)
    {
        SetVisitor setVisitor = new SetVisitor(getConfig());
        SqlContext context = setVisitor.visit(expr.getTree());
        getSqlBuilder().addSet(context);
        return this;
    }
    //endregion

    //region [WHERE]
    public LUpdate4<T1, T2, T3, T4> where(@Expr Func4<T1, T2, T3, T4, Boolean> func)
    {
        throw new RuntimeException();
    }

    public LUpdate4<T1, T2, T3, T4> where(ExprTree<Func4<T1, T2, T3, T4, Boolean>> expr)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext context = whereVisitor.visit(expr.getTree());
        getSqlBuilder().addWhere(context);
        return this;
    }
    //endregion
}
