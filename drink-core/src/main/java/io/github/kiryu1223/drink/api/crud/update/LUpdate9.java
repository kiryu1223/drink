package io.github.kiryu1223.drink.api.crud.update;

import io.github.kiryu1223.drink.api.crud.builder.UpdateSqlBuilder;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.visitor.SetVisitor;
import io.github.kiryu1223.drink.core.visitor.WhereVisitor;
import io.github.kiryu1223.expressionTree.delegate.Action9;
import io.github.kiryu1223.expressionTree.delegate.Func10;
import io.github.kiryu1223.expressionTree.delegate.Func9;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LUpdate9<T1, T2, T3, T4, T5, T6, T7, T8, T9> extends UpdateBase
{
    public LUpdate9(UpdateSqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    //region [JOIN]
    public <Tn> LUpdate10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> innerJoin(Class<Tn> target, @Expr Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LUpdate10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> innerJoin(Class<Tn> target, ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return new LUpdate10<>(getSqlBuilder());
    }

    public <Tn> LUpdate10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> leftJoin(Class<Tn> target, @Expr Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LUpdate10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> leftJoin(Class<Tn> target, ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return new LUpdate10<>(getSqlBuilder());
    }

    public <Tn> LUpdate10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> rightJoin(Class<Tn> target, @Expr Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean> func)
    {
        throw new RuntimeException();
    }

    public <Tn> LUpdate10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> rightJoin(Class<Tn> target, ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return new LUpdate10<>(getSqlBuilder());
    }
    //endregion

    //region [SET]
    public LUpdate9<T1, T2, T3, T4, T5, T6, T7, T8, T9> set(@Expr Action9<T1, T2, T3, T4, T5, T6, T7, T8, T9> action)
    {
        throw new RuntimeException();
    }

    public LUpdate9<T1, T2, T3, T4, T5, T6, T7, T8, T9> set(ExprTree<Action9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> expr)
    {
        SetVisitor setVisitor = new SetVisitor(getConfig());
        SqlContext context = setVisitor.visit(expr.getTree());
        getSqlBuilder().addSet(context);
        return this;
    }
    //endregion

    //region [WHERE]
    public LUpdate9<T1, T2, T3, T4, T5, T6, T7, T8, T9> where(@Expr Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean> func)
    {
        throw new RuntimeException();
    }

    public LUpdate9<T1, T2, T3, T4, T5, T6, T7, T8, T9> where(ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean>> expr)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext context = whereVisitor.visit(expr.getTree());
        getSqlBuilder().addWhere(context);
        return this;
    }
    //endregion
}