package io.github.kiryu1223.drink.core.api.crud.create;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.core.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Action1;
import io.github.kiryu1223.expressionTree.expressions.annos.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LInsert<T> extends InsertBase
{
    private final Class<T> t;

    public LInsert(IConfig c, Class<T> t)
    {
        super(c);
        this.t = t;
    }

    public LInsert<T> set(@Expr Action1<T> action)
    {
        throw new NotCompiledException();
    }

    public LInsert<T> set(ExprTree<Action1<T>> action)
    {
        throw new NotCompiledException();
    }

    @Override
    protected Class<T> getTableType()
    {
        return t;
    }
}
