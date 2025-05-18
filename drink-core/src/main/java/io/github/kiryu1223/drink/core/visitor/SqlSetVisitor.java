package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.expressionTree.expressions.ResultThrowVisitor;

public class SqlSetVisitor extends ResultThrowVisitor<ISqlExpression>
{
    protected final IConfig config;
    protected final SqlExpressionFactory factory;

    public SqlSetVisitor(IConfig config, SqlExpressionFactory factory)
    {
        this.config = config;
        this.factory = factory;
    }


}
