package io.github.kiryu1223.drink.core.api.crud.read.pivot;

import io.github.kiryu1223.drink.core.api.Result;
import io.github.kiryu1223.drink.core.api.crud.read.LQuery;
import io.github.kiryu1223.drink.core.api.crud.read.QueryBase;
import io.github.kiryu1223.drink.core.sqlBuilder.QuerySqlBuilder;
import io.github.kiryu1223.expressionTree.delegate.Func1;

public class PivotQuery<T> extends LQuery<T> {
    public PivotQuery(QuerySqlBuilder sqlBuilder) {
        super(sqlBuilder);
    }


}
