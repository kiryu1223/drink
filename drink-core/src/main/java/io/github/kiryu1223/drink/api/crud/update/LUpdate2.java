package io.github.kiryu1223.drink.api.crud.update;

import io.github.kiryu1223.drink.core.sqlBuilder.UpdateSqlBuilder;
import io.github.kiryu1223.drink.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Action2;
import io.github.kiryu1223.expressionTree.delegate.Func2;
import io.github.kiryu1223.expressionTree.delegate.Func3;
import io.github.kiryu1223.expressionTree.expressions.annos.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LUpdate2<T1, T2> extends UpdateBase
{
    // region [INIT]
    public LUpdate2(UpdateSqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }
    //endregion

    //region [JOIN]
    public <Tn> LUpdate3<T1, T2, Tn> innerJoin(Class<Tn> target, @Expr Func3<T1, T2, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LUpdate3<T1, T2, Tn> innerJoin(Class<Tn> target, ExprTree<Func3<T1, T2, Tn, Boolean>> expr)
    {
        join(JoinType.INNER, target, expr);
        return new LUpdate3<>(getSqlBuilder());
    }

    public <Tn> LUpdate3<T1, T2, Tn> leftJoin(Class<Tn> target, @Expr Func3<T1, T2, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LUpdate3<T1, T2, Tn> leftJoin(Class<Tn> target, ExprTree<Func3<T1, T2, Tn, Boolean>> expr)
    {
        join(JoinType.LEFT, target, expr);
        return new LUpdate3<>(getSqlBuilder());
    }

    public <Tn> LUpdate3<T1, T2, Tn> rightJoin(Class<Tn> target, @Expr Func3<T1, T2, Tn, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public <Tn> LUpdate3<T1, T2, Tn> rightJoin(Class<Tn> target, ExprTree<Func3<T1, T2, Tn, Boolean>> expr)
    {
        join(JoinType.RIGHT, target, expr);
        return new LUpdate3<>(getSqlBuilder());
    }
    //endregion

    //region [SET]
    public LUpdate2<T1, T2> set(@Expr Action2<T1, T2> action)
    {
        throw new NotCompiledException();
    }

    public LUpdate2<T1, T2> set(ExprTree<Action2<T1, T2>> expr)
    {
        set(expr.getTree());
        return this;
    }
    //endregion

    //region [WHERE]
    public LUpdate2<T1, T2> where(@Expr Func2<T1, T2, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LUpdate2<T1, T2> where(ExprTree<Func2<T1, T2, Boolean>> expr)
    {
        where(expr.getTree());
        return this;
    }
    //endregion
}
