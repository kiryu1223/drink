package io.github.kiryu1223.aot.data;

public class AnonymousClassData extends ClassData
{
    private String name;
    private boolean unsafeAllocated = true;
    private boolean allDeclaredFields = true;
    private boolean allPublicMethods = true;

    public AnonymousClassData(String name)
    {
        super(name);
        this.name = name;
    }
}
