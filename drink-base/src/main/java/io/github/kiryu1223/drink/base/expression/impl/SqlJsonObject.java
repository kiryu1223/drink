package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlJsonObject;
import io.github.kiryu1223.drink.base.expression.JsonProperty;
import io.github.kiryu1223.drink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SqlJsonObject implements ISqlJsonObject
{
    protected final ISqlColumnExpression columnExpression;
    protected int index = -1;
    protected final List<JsonProperty> jsonPropertyList = new ArrayList<>();

    public SqlJsonObject(ISqlColumnExpression columnExpression)
    {
        this.columnExpression = columnExpression;
    }

    @Override
    public ISqlColumnExpression getColumnExpression()
    {
        return columnExpression;
    }

    @Override
    public List<JsonProperty> getJsonPropertyList()
    {
        return jsonPropertyList;
    }

    @Override
    public void setIndex(int index)
    {
        this.index = index;
    }

    @Override
    public int getIndex()
    {
        return index;
    }

    protected String $()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("$");
        if (index >= 0)
        {
            builder.append(String.format("[%d]", index));
        }
        return builder.toString();
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
    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values)
    {
        StringBuilder builder = new StringBuilder();

        builder.append($()).append(".");

        String join = jsonPropertyList
                .stream()
                .map(j -> j.toProperty())
                .collect(Collectors.joining("."));

        builder.append(join);

        String property = builder.toString();

        return String.join(" ->> ",
                columnExpression.getSqlAndValue(config, values),
                "'" + property + "'"
        );
    }
}
