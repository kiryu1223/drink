package io.github.kiryu1223.aot.data;

public class NormalClassData extends ClassData
{
    private String name;

    protected boolean allPublicConstructors = true;
    protected boolean allDeclaredFields = true;
    protected boolean allPublicMethods = true;

    public NormalClassData(String name)
    {
        super(name);
        this.name = name;
    }
}
