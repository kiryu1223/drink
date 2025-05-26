package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.session.SqlSession;

import java.util.*;

public class SubQueryBuilder {
    private final IConfig config;
    // 需要被注入的字段
    private final FieldMetaData fieldMetaData;
    // 子查询
    private final QuerySqlBuilder querySqlBuilder;
    private final List<SubQueryBuilder> subQueryBuilders=new ArrayList<>();

    public SubQueryBuilder(IConfig config, FieldMetaData fieldMetaData, QuerySqlBuilder querySqlBuilder) {
        this.config = config;
        this.fieldMetaData = fieldMetaData;
        this.querySqlBuilder = querySqlBuilder;
    }

    public List<SubQueryBuilder> getSubQueryBuilders() {
        return subQueryBuilders;
    }

    public void subQuery(
            // 会话
            SqlSession session,
            // 原始集合
            Collection<?> sources,
            // 上下文参数
            List<Map<String, List<Object>>> valueContext
    ) {

    }
}
