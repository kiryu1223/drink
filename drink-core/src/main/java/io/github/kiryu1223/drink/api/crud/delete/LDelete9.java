package io.github.kiryu1223.drink.api.crud.delete;

import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.sqlBuilder.DeleteSqlBuilder;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.drink.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Func10;
import io.github.kiryu1223.expressionTree.delegate.Func9;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LDelete9<T1, T2, T3, T4, T5, T6, T7, T8, T9> extends DeleteBase
{
    public LDelete9(DeleteSqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }
    
    //region [JOIN]
    public <Tn> LDelete10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> innerJoin(Class<Tn> target, @Expr Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LDelete10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> innerJoin(Class<Tn> target, ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return new LDelete10<>(getSqlBuilder());
    }

    public <Tn> LDelete10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> leftJoin(Class<Tn> target, @Expr Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LDelete10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> leftJoin(Class<Tn> target, ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return new LDelete10<>(getSqlBuilder());
    }

    public <Tn> LDelete10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> rightJoin(Class<Tn> target, @Expr Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LDelete10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn> rightJoin(Class<Tn> target, ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return new LDelete10<>(getSqlBuilder());
    }
    //endregion

    // region [WHERE]
    public LDelete9<T1, T2, T3, T4, T5, T6, T7, T8, T9> where(@Expr Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LDelete9<T1, T2, T3, T4, T5, T6, T7, T8, T9> where(ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean>> expr)
    {
        NormalVisitor normalVisitor = new NormalVisitor(getConfig());
        SqlContext context = normalVisitor.visit(expr.getTree());
        getSqlBuilder().addWhere(context);
        return this;
    }
    // endregion

    public <R> LDelete9<T1, T2, T3, T4, T5, T6, T7, T8, T9> selectDelete(@Expr(Expr.BodyType.Expr) Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> LDelete9<T1, T2, T3, T4, T5, T6, T7, T8, T9> selectDelete(ExprTree<Func9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>> expr)
    {
        Class<?> returnType = expr.getTree().getReturnType();
        selectDeleteTable(returnType);
        return this;
    }
}
