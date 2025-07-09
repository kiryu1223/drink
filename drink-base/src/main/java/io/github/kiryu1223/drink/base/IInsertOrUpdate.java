package io.github.kiryu1223.drink.base;

import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;

import java.util.List;

public interface IInsertOrUpdate {

    boolean apply();

    default String insertOrUpdate(
            // 元数据
            MetaData metaData,
            // 插入字段
            List<FieldMetaData> insertColumns,
            // 冲突字段
            List<ISqlColumnExpression> conflictColumns,
            // 更新字段
            List<ISqlColumnExpression> updateColumns
    ) {
        return "";
    }
}
