package io.github.kiryu1223.drink.base.toBean.build;

import java.util.Map;

public class ExtensionObject<T> {
    private final T object;
    private final Map<String, ExtensionField> extensionValueFieldMap;
    private final Map<String, ExtensionField> extensionKeyFieldMap;

    public ExtensionObject(T object, Map<String, ExtensionField> extensionValueFieldMap, Map<String, ExtensionField> extensionKeyFieldMap) {
        this.object = object;
        this.extensionValueFieldMap = extensionValueFieldMap;
        this.extensionKeyFieldMap = extensionKeyFieldMap;
    }

    public Object getObject() {
        return object;
    }

    public Map<String, ExtensionField> getExtensionValueFieldMap() {
        return extensionValueFieldMap;
    }

    public Map<String, ExtensionField> getExtensionKeyFieldMap() {
        return extensionKeyFieldMap;
    }
}
