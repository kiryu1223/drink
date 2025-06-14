package io.github.kiryu1223.drink.base;

import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;

import java.util.List;

public interface IInsertOrUpdate {

    boolean apply();

    default String insertOrUpdate(MetaData metaData, List<FieldMetaData> notIgnoreAndNavigateFields, List<ISqlColumnExpression> conflictColumns, List<ISqlColumnExpression> updateColumns) {
        return "";
    }
}
