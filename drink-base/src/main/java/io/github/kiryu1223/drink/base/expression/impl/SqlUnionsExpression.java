package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlUnionExpression;
import io.github.kiryu1223.drink.base.expression.ISqlUnionsExpression;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.List;

public class SqlUnionsExpression implements ISqlUnionsExpression {

    protected final List<ISqlUnionExpression> unions = new ArrayList<>();


    @Override
    public List<ISqlUnionExpression> getUnions() {
        return unions;
    }

    @Override
    public void addUnion(ISqlUnionExpression union) {
        unions.add(union);
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        if (unions.isEmpty()) return "";
        List<String> strings = new ArrayList<>(unions.size());
        for (ISqlUnionExpression union : unions) {
            strings.add(union.getSqlAndValue(config, values));
        }
//        for (int i = unions.size() - 1; i >= 0; i--) {
//            ISqlUnionExpression union = unions.get(i);
//            strings.add(union.getSqlAndValue(config, values));
//        }
        return String.join(" ", strings);
    }
}
