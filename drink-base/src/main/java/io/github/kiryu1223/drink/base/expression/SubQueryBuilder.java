package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.session.SqlSession;

import java.util.*;

public class SubQueryBuilder {
    private final IConfig config;
    // 需要被注入的字段
    private final FieldMetaData fieldMetaData;
    // 子查询
    private final ISqlQueryableExpression queryableExpression;

    public SubQueryBuilder(IConfig config, FieldMetaData fieldMetaData, ISqlQueryableExpression queryableExpression) {
        this.config = config;
        this.fieldMetaData = fieldMetaData;
        this.queryableExpression = queryableExpression;
    }

    public void subQuery(
            // 会话
            SqlSession session,
            // 原始集合
            Collection<?> sources,
            // 上下文参数
            Map<String, List<Object>> valueContext
    ) {
        Map<String, List<Object>> need=new HashMap<>();
        queryableExpression.accept(new SqlTreeVisitor(){
            @Override
            public void visit(ISqlColumnExpression expr)
            {
                super.visit(expr);
                if (expr instanceof SubQueryValue)
                {
                    SubQueryValue subQueryValue = (SubQueryValue) expr;
                    String keyName = subQueryValue.getKeyName();
                    if(!need.containsKey(keyName))
                    {
                        need.put(keyName,valueContext.get(keyName));
                    }
                }
            }
        });


    }
}
