package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.config.Config;


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
