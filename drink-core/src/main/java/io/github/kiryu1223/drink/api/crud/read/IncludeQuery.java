package io.github.kiryu1223.drink.api.crud.read;

import io.github.kiryu1223.drink.core.builder.IncludeSet;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.expressions.Expr;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;

import java.util.Collection;

public class IncludeQuery<T, TPreviousProperty> extends LQuery<T>
{
    //private final IncludeSet includeSet;

    public IncludeQuery(QuerySqlBuilder sqlBuilder)
    {
        super(sqlBuilder);
    }

    public <TProperty> IncludeQuery<T, TProperty> thenInclude(@Expr(Expr.BodyType.Expr) Func1<TPreviousProperty, TProperty> expr,@Expr(Expr.BodyType.Expr) Func1<TProperty, Boolean> cond)
    {
        throw new RuntimeException();
    }

    public <TProperty> IncludeQuery<T, TProperty> thenInclude(@Expr(Expr.BodyType.Expr) Func1<TPreviousProperty, TProperty> expr)
    {
        throw new RuntimeException();
    }

    public <TProperty> IncludeQuery<T, TProperty> thenIncludes(@Expr(Expr.BodyType.Expr) Func1<TPreviousProperty, Collection<TProperty>> expr,@Expr(Expr.BodyType.Expr) Func1<TProperty, Boolean> cond)
    {
        throw new RuntimeException();
    }

    public <TProperty> IncludeQuery<T, TProperty> thenIncludes(@Expr(Expr.BodyType.Expr) Func1<TPreviousProperty, Collection<TProperty>> expr)
    {
        throw new RuntimeException();
    }

    public <TProperty> IncludeQuery<T, TProperty> thenInclude(ExprTree<Func1<TPreviousProperty, TProperty>> expr, ExprTree<Func1<TProperty, Boolean>> cond)
    {
        include(expr.getTree(),cond.getTree());
        return new IncludeQuery<>(getSqlBuilder());
    }

    public <TProperty> IncludeQuery<T, TProperty> thenInclude(ExprTree<Func1<TPreviousProperty, TProperty>> expr)
    {
        include(expr.getTree());
        return new IncludeQuery<>(getSqlBuilder());
    }

    public <TProperty> IncludeQuery<T, TProperty> thenIncludes(ExprTree<Func1<TPreviousProperty, Collection<TProperty>>> expr, ExprTree<Func1<TProperty, Boolean>> cond)
    {
        include(expr.getTree(),cond.getTree());
        return new IncludeQuery<>(getSqlBuilder());
    }

    public <TProperty> IncludeQuery<T, TProperty> thenIncludes(ExprTree<Func1<TPreviousProperty, Collection<TProperty>>> expr)
    {
        include(expr.getTree());
        return new IncludeQuery<>(getSqlBuilder());
    }
}
