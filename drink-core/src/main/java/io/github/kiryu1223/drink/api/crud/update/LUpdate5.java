package io.github.kiryu1223.drink.api.crud.update;

import io.github.kiryu1223.drink.core.sqlBuilder.UpdateSqlBuilder;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.visitor.SetVisitor;
import io.github.kiryu1223.drink.core.visitor.WhereVisitor;
import io.github.kiryu1223.drink.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Action5;
import io.github.kiryu1223.expressionTree.delegate.Func5;
import io.github.kiryu1223.expressionTree.delegate.Func6;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LUpdate5<T1, T2, T3, T4, T5> extends UpdateBase
{
    public LUpdate5(UpdateSqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    //region [JOIN]
    public <Tn> LUpdate6<T1, T2, T3, T4, T5, Tn> innerJoin(Class<Tn> target, @Expr Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LUpdate6<T1, T2, T3, T4, T5, Tn> innerJoin(Class<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return new LUpdate6<>(getSqlBuilder());
    }

    public <Tn> LUpdate6<T1, T2, T3, T4, T5, Tn> leftJoin(Class<Tn> target, @Expr Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LUpdate6<T1, T2, T3, T4, T5, Tn> leftJoin(Class<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return new LUpdate6<>(getSqlBuilder());
    }

    public <Tn> LUpdate6<T1, T2, T3, T4, T5, Tn> rightJoin(Class<Tn> target, @Expr Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LUpdate6<T1, T2, T3, T4, T5, Tn> rightJoin(Class<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return new LUpdate6<>(getSqlBuilder());
    }
    //endregion

    //region [SET]
    public LUpdate5<T1, T2, T3, T4, T5> set(@Expr Action5<T1, T2, T3, T4, T5> action)
    {
        throw new NotCompiledException();
    }

    public LUpdate5<T1, T2, T3, T4, T5> set(ExprTree<Action5<T1, T2, T3, T4, T5>> expr)
    {
        SetVisitor setVisitor = new SetVisitor(getConfig());
        SqlContext context = setVisitor.visit(expr.getTree());
        getSqlBuilder().addSet(context);
        return this;
    }
    //endregion

    //region [WHERE]
    public LUpdate5<T1, T2, T3, T4, T5> where(@Expr Func5<T1, T2, T3, T4, T5, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LUpdate5<T1, T2, T3, T4, T5> where(ExprTree<Func5<T1, T2, T3, T4, T5, Boolean>> expr)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext context = whereVisitor.visit(expr.getTree());
        getSqlBuilder().addWhere(context);
        return this;
    }
    //endregion
}
