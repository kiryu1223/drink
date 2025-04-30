package io.github.kiryu1223.drink.base;

import java.util.List;

public class SqlOption {
    protected final List<String> ignoreFilterIds;
    protected final boolean ignoreFilterAll;

    public SqlOption(List<String> ignoreFilterIds, boolean ignoreFilterAll) {
        this.ignoreFilterIds = ignoreFilterIds;
        this.ignoreFilterAll = ignoreFilterAll;
    }

    public List<String> getIgnoreFilterIds() {
        return ignoreFilterIds;
    }

    public boolean isIgnoreFilterAll() {
        return ignoreFilterAll;
    }
}
