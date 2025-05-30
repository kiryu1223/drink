package io.github.kiryu1223.drink.base.toBean.build;

import java.util.ArrayList;
import java.util.List;

public class JdbcResult<T> {
    private final List<T> result = new ArrayList<>();
    private final List<ExtensionObject<T>> extensionResult = new ArrayList<>();

    public List<T> getResult() {
        return result;
    }

    public void addResult(T result) {
        this.result.add(result);
    }

    public void addResult(ExtensionObject<T> result) {
        this.extensionResult.add(result);
    }

    public List<ExtensionObject<T>> getExtensionResult() {
        return extensionResult;
    }
}
