package io.github.kiryu1223.drink.core.sqlBuilder;


import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.List;

public class UnionBuilder implements ISqlBuilder {
    private final IConfig config;
    private final ISqlUnionQueryableExpression unionQueryable;
    private final List<String> ignoreFilterIds = new ArrayList<>();
    private boolean ignoreFilterAll = false;

    public UnionBuilder(IConfig config, ISqlUnionQueryableExpression unionQueryable) {
        this.config = config;
        this.unionQueryable = unionQueryable;
    }

    @Override
    public ISqlExpression getSqlExpression() {
        return unionQueryable;
    }

    public ISqlUnionQueryableExpression getUnionQueryable() {
        return unionQueryable;
    }

    @Override
    public List<String> getIgnoreFilterIds() {
        return ignoreFilterIds;
    }

    @Override
    public boolean isIgnoreFilterAll() {
        return ignoreFilterAll;
    }

    public void setIgnoreFilterAll(boolean ignoreFilterAll) {
        this.ignoreFilterAll = ignoreFilterAll;
    }

    public void addIgnoreFilterId(String filterId) {
        ignoreFilterIds.add(filterId);
    }

    public void addUnion(ISqlQueryableExpression queryable, boolean isAll) {
        unionQueryable.addQueryable(queryable, isAll);
    }

    public boolean isSingle() {
        return unionQueryable.getQueryable().get(0).getSelect().isSingle();
    }

    public List<FieldMetaData> getMappingData() {
        return unionQueryable.getQueryable().get(0).getMappingData(config);
    }

    public <T> Class<T> getTargetClass() {
        return (Class<T>) unionQueryable.getMainTableClass();
    }

    @Override
    public IConfig getConfig() {
        return config;
    }
}
