package io.github.kiryu1223.plugin.service;

public class AnonymousClassData extends ClassData
{
    protected boolean unsafeAllocated = true;

    public AnonymousClassData(String name)
    {
        super(name);
    }
}
