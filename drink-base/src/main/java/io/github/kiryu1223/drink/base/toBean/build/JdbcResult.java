package io.github.kiryu1223.drink.base.toBean.build;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.cast;

public class JdbcResult<T> {
    private final List<T> result = new ArrayList<>();
    private final List<ExtensionObject> extensionValueResult = new ArrayList<>();
    private final List<ExtensionObject> extensionKeyResult = new ArrayList<>();

    public List<T> getResult() {
        if (!result.isEmpty()) {
            return result;
        }
        else {
            if (extensionValueResult.isEmpty())
            {
                return Collections.emptyList();
            }
            else
            {
                List<T> rs = new ArrayList<>();
                for (ExtensionObject extensionObject : extensionValueResult) {
                    rs.addAll(cast(extensionObject.getObjects()));
                }
                return rs;
            }
        }
    }

    public void addResult(T result) {
        this.result.add(result);
    }

    public void addValueResult(ExtensionObject result) {
        this.extensionValueResult.add(result);
    }

    public void addKeyResult(ExtensionObject result) {
        this.extensionKeyResult.add(result);
    }

    public List<ExtensionObject> getExtensionValueResult() {
        return extensionValueResult;
    }

    public List<ExtensionObject> getExtensionKeyResult() {
        return extensionKeyResult;
    }
}
