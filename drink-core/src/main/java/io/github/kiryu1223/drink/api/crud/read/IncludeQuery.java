package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

import java.util.Collection;

public class IncludeQuery<T, TPreviousProperty> extends LQuery<T>
{
    public IncludeQuery(QuerySqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    public <TProperty> IncludeQuery<T, TProperty> thenInclude(@Expr(Expr.BodyType.Expr) Func1<TPreviousProperty, TProperty> expr, Func1<TProperty, Boolean> cond)
    {
        throw new RuntimeException();
    }

    public <TProperty> IncludeQuery<T, TProperty> thenInclude(@Expr(Expr.BodyType.Expr) Func1<TPreviousProperty, TProperty> expr)
    {
        throw new RuntimeException();
    }

    public <TProperty> IncludeQuery<T, TProperty> thenIncludes(@Expr(Expr.BodyType.Expr) Func1<TPreviousProperty, Collection<TProperty>> expr, Func1<TProperty, Boolean> cond)
    {
        throw new RuntimeException();
    }

    public <TProperty> IncludeQuery<T, TProperty> thenIncludes(@Expr(Expr.BodyType.Expr) Func1<TPreviousProperty, Collection<TProperty>> expr)
    {
        throw new RuntimeException();
    }
}
