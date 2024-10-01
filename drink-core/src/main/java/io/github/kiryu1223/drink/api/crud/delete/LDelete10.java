package io.github.kiryu1223.drink.api.crud.delete;

import io.github.kiryu1223.drink.core.sqlBuilder.DeleteSqlBuilder;
import io.github.kiryu1223.drink.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Func10;
import io.github.kiryu1223.expressionTree.expressions.annos.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LDelete10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extends DeleteBase
{
    public LDelete10(DeleteSqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    // region [WHERE]
    public LDelete10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> where(@Expr Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean> func)
    {
        throw new NotCompiledException();
    }

    public LDelete10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> where(ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean>> expr)
    {
        where(expr.getTree());
        return this;
    }
    // endregion

    public <R> LDelete10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> selectDelete(@Expr(Expr.BodyType.Expr) Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> expr)
    {
        throw new NotCompiledException();
    }

    public <R> LDelete10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> selectDelete(ExprTree<Func10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>> expr)
    {
        Class<?> returnType = expr.getTree().getReturnType();
        selectDeleteTable(returnType);
        return this;
    }
}
