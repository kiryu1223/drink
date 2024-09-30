package io.github.kiryu1223.plugin.service;

public class ClassData
{
    protected String name;
    protected boolean allDeclaredFields = true;
    protected boolean allPublicMethods = true;

    public ClassData(String name)
    {
        this.name = name;
    }
}
