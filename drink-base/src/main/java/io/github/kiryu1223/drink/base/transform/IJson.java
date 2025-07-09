package io.github.kiryu1223.drink.base.transform;

import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface IJson
{
    SqlExpressionFactory getSqlExpressionFactory();

    default String $(List<String> names)
    {
        return String.format("'$.%s'", String.join(".",names));
    }

    /**
     * json对象属性
     * <p>
     * SELECT t.field ->> '$.name' FROM {table} AS t
     * <p>
     * ->  获取带""的值
     * <p>
     * ->> 解开""
     */
    default ISqlExpression jsonProperty(ISqlExpression left, List<FieldMetaData> fieldMetaDataList) {
        SqlExpressionFactory factory = getSqlExpressionFactory();
        List<String> names = fieldMetaDataList.stream().map(f -> f.getColumnName()).collect(Collectors.toList());
        return factory.template(
                Arrays.asList("", String.format(" ->> %s", $(names))),
                Collections.singletonList(left)
        );
    }
}
