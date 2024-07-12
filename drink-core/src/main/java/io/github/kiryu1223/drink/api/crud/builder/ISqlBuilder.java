package io.github.kiryu1223.drink.api.crud.builder;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public interface ISqlBuilder
{
    Config getConfig();

    String getSql();

    String getSqlAndValue(List<Object> values);
}
