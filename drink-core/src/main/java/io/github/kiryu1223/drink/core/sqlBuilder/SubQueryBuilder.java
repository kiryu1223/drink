package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SubQueryBuilder
{
    private final IConfig config;

    public SubQueryBuilder(IConfig config)
    {
        this.config = config;
    }

    public void subQuery(
            // 原始集合
            Collection<?> sources,
            // 子查询需要映射到的字段
            FieldMetaData subQueryField,
            // 上下文参数
            List<Map<String,List<Object>>> valueContext
    )
    {

    }
}
