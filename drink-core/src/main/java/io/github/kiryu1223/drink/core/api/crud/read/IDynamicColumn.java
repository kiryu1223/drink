package io.github.kiryu1223.drink.core.api.crud.read;

import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.metaData.MetaDataCache;

public interface IDynamicColumn {
    default <T> T column(String name, Class<T> type) {
        MetaData metaData = MetaDataCache.getMetaData(getClass());
        FieldMetaData fieldMetaData = metaData.getFieldMetaDataByFieldName(name);
        return fieldMetaData.getValueByObject(this);
    }
}
