package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.AsNameManager;
import io.github.kiryu1223.drink.base.expression.ISqlTableRefExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SqlTableRefExpression implements ISqlTableRefExpression {
    private String name;

    protected SqlTableRefExpression(String name) {
        this.name = name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisPlayName() {
        Map<ISqlTableRefExpression, String> asNameStringMap = AsNameManager.get();
        String s = asNameStringMap.get(this);
        if (s == null) {
            Set<String> strings = new HashSet<>(asNameStringMap.values());
            s = name;
            int index = 1;
            while (strings.contains(s)) {
                s = name + index++;
            }
            asNameStringMap.put(this, s);
        }
        return s;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        return getDisPlayName();
    }
}
