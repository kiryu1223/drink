package io.github.kiryu1223.drink.base.toBean.build;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExtensionObject
{
    private final List<Object> objects = new ArrayList<>();
    private final Map<String, ExtensionField> extensionFieldMap;

    public ExtensionObject(Map<String, ExtensionField> extensionValueFieldMap)
    {
        this.extensionFieldMap = extensionValueFieldMap;
    }

    public List<Object> getObjects()
    {
        return objects;
    }

    public void addObject(Object o)
    {
        objects.add(o);
    }

    public Map<String, ExtensionField> getExtensionFieldMap()
    {
        return extensionFieldMap;
    }
}
