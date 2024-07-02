package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.config.Config;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.isProperty;
import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.propertyName;

public class WhereVisitor extends SqlVisitor
{
    public WhereVisitor(Config config)
    {
        super(config);
    }

    @Override
    protected WhereVisitor getSelf()
    {
        return new WhereVisitor(config);
    }
}
