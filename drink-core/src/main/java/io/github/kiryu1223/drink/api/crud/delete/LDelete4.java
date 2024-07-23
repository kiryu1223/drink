package io.github.kiryu1223.drink.api.crud.delete;

import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.sqlBuilder.DeleteSqlBuilder;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.drink.core.visitor.WhereVisitor;
import io.github.kiryu1223.drink.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Func3;
import io.github.kiryu1223.expressionTree.delegate.Func4;
import io.github.kiryu1223.expressionTree.delegate.Func5;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LDelete4<T1,T2,T3,T4> extends DeleteBase
{
    public LDelete4(DeleteSqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    //region [JOIN]
    public <Tn> LDelete5<T1, T2, T3, T4, Tn> innerJoin(Class<Tn> target, @Expr Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LDelete5<T1, T2, T3, T4, Tn> innerJoin(Class<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return new LDelete5<>(getSqlBuilder());
    }

    public <Tn> LDelete5<T1, T2, T3, T4, Tn> leftJoin(Class<Tn> target, @Expr Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LDelete5<T1, T2, T3, T4, Tn> leftJoin(Class<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return new LDelete5<>(getSqlBuilder());
    }

    public <Tn> LDelete5<T1, T2, T3, T4, Tn> rightJoin(Class<Tn> target, @Expr Func5<T1, T2, T3, T4, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LDelete5<T1, T2, T3, T4, Tn> rightJoin(Class<Tn> target, ExprTree<Func5<T1, T2, T3, T4, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return new LDelete5<>(getSqlBuilder());
    }
    //endregion

    // region [WHERE]
    public LDelete4<T1,T2,T3,T4> where(@Expr Func4<T1,T2,T3,T4, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LDelete4<T1,T2,T3,T4> where(ExprTree<Func4<T1,T2,T3,T4, Boolean>> expr)
    {
        WhereVisitor whereVisitor = new WhereVisitor(getConfig());
        SqlContext context = whereVisitor.visit(expr.getTree());
        getSqlBuilder().addWhere(context);
        return this;
    }
    // endregion

    public <R> LDelete4<T1,T2,T3,T4> selectDelete(@Expr(Expr.BodyType.Expr) Func4<T1,T2,T3,T4, R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> LDelete4<T1,T2,T3,T4> selectDelete(ExprTree<Func4<T1,T2,T3,T4, R>> expr)
    {
        Class<?> returnType = expr.getTree().getReturnType();
        selectDeleteTable(returnType);
        return this;
    }
}
