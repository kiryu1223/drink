package io.github.kiryu1223.drink.visitor;


import io.github.kiryu1223.drink.base.IConfig;

public class NormalVisitor extends SqlVisitor
{
    public NormalVisitor(IConfig config)
    {
        super(config);
    }

    public NormalVisitor(IConfig config, int offset)
    {
        super(config, offset);
    }

    @Override
    protected NormalVisitor getSelf()
    {
        return new NormalVisitor(config);
    }
}
