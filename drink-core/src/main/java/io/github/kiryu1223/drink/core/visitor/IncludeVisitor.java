package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.config.Config;

public class IncludeVisitor extends SqlVisitor
{
    public IncludeVisitor(Config config)
    {
        super(config);
    }

    @Override
    protected SqlVisitor getSelf()
    {
        return new IncludeVisitor(config);
    }
}
