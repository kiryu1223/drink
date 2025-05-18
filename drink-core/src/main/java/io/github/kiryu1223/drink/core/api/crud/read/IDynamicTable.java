package io.github.kiryu1223.drink.core.api.crud.read;

import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.expressionTree.util.ReflectUtil;

public interface IDynamicTable
{
    default <T> T column(String name, Class<T> type) {
        return ReflectUtil.getFieldValue(this, name);
    }
}
