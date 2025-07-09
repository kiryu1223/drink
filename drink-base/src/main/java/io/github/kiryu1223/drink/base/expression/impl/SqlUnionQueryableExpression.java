package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.expression.ISqlUnionQueryableExpression;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.List;

public class SqlUnionQueryableExpression implements ISqlUnionQueryableExpression {
    protected final List<ISqlQueryableExpression> queryable;
    protected final List<Boolean> unions;

    protected SqlUnionQueryableExpression(List<ISqlQueryableExpression> queryable, List<Boolean> unions) {
        this.queryable = queryable;
        this.unions = unions;
    }

    @Override
    public List<ISqlQueryableExpression> getQueryable() {
        return queryable;
    }

    @Override
    public List<Boolean> getUnions() {
        return unions;
    }

    @Override
    public List<FieldMetaData> getMappingData(IConfig config) {
        return queryable.get(0).getMappingData(config);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        List<String> strings = new ArrayList<>(queryable.size() + unions.size());
        for (int i = 0; i < queryable.size(); i++) {
            ISqlQueryableExpression iSqlQueryableExpression = queryable.get(i);
            strings.add("(" + iSqlQueryableExpression.getSqlAndValue(config, values) + ")");
            if (i < unions.size()) {
                strings.add(unions.get(i) ? "UNION ALL" : "UNION");
            }
        }
        return String.join(" ", strings);
    }
}
