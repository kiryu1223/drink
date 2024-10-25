package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.List;

public interface ISqlBuilder
{
    IConfig getConfig();

    String getSql();

    String getSqlAndValue(List<Object> values);
}
