package io.github.kiryu1223.drink.api.crud.create;


import io.github.kiryu1223.drink.api.crud.builder.InsertSqlBuilder;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.delegate.Action1;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

public class LInsert<T> extends InsertBase
{
    private final Class<T> t;

    public LInsert(Config c, Class<T> t)
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
