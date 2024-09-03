package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.config.Config;

public class NormalVisitor extends SqlVisitor
{
    public NormalVisitor(Config config)
    {
        super(config);
    }

    public NormalVisitor(Config config, int offset)
    {
        super(config, offset);
    }

    @Override
    protected NormalVisitor getSelf()
    {
        return new NormalVisitor(config);
    }
}
