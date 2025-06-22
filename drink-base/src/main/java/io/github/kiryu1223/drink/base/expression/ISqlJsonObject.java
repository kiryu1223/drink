package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

public interface ISqlJsonObject extends ISqlExpression
{
    ISqlColumnExpression getColumnExpression();

    List<JsonProperty> getJsonPropertyList();

    void setIndex(int index);

    int getIndex();

    default void addJsonProperty(JsonProperty jsonProperty)
    {
        getJsonPropertyList().add(jsonProperty);
    }

    @Override
    default ISqlJsonObject copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlJsonObject sqlJsonProperty = factory.jsonProperty(getColumnExpression());
        sqlJsonProperty.setIndex(getIndex());
        for (JsonProperty jsonProperty : getJsonPropertyList())
        {
            sqlJsonProperty.addJsonProperty(jsonProperty);
        }
        return sqlJsonProperty;
    }
}
