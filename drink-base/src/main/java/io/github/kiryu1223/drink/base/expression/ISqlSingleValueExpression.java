package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.metaData.IConverter;
import io.github.kiryu1223.drink.base.metaData.PropertyMetaData;

import java.util.List;

public interface ISqlSingleValueExpression extends ISqlValueExpression
{
    Object getValue();

    String getSqlAndValue(IConfig config, List<Object> values, IConverter<?, ?> converter, PropertyMetaData propertyMetaData);

    @Override
    default ISqlSingleValueExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.value(getValue());
    }
}
