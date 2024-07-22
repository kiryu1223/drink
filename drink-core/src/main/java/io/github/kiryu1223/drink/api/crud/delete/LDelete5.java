package io.github.kiryu1223.drink.api.crud.delete;

import io.github.kiryu1223.drink.api.crud.builder.DeleteSqlBuilder;
import io.github.kiryu1223.drink.core.context.JoinType;
import io.github.kiryu1223.drink.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Func6;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LDelete5<T1, T2, T3, T4, T5> extends DeleteBase
{
    public LDelete5(DeleteSqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    //region [JOIN]
    public <Tn> LDelete6<T1, T2, T3, T4, T5, Tn> innerJoin(Class<Tn> target, @Expr Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LDelete6<T1, T2, T3, T4, T5, Tn> innerJoin(Class<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return new LDelete6<>(getSqlBuilder());
    }

    public <Tn> LDelete6<T1, T2, T3, T4, T5, Tn> leftJoin(Class<Tn> target, @Expr Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LDelete6<T1, T2, T3, T4, T5, Tn> leftJoin(Class<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return new LDelete6<>(getSqlBuilder());
    }

    public <Tn> LDelete6<T1, T2, T3, T4, T5, Tn> rightJoin(Class<Tn> target, @Expr Func6<T1, T2, T3, T4, T5, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LDelete6<T1, T2, T3, T4, T5, Tn> rightJoin(Class<Tn> target, ExprTree<Func6<T1, T2, T3, T4, T5, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return new LDelete6<>(getSqlBuilder());
    }
    //endregion
}
