package io.github.kiryu1223.drink.core.builder;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IResultSetValueGetter
{
    Object getValueByName(ResultSet resultSet, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException, SQLException;

    Object getValueByIndex(ResultSet resultSet,int index, Class<?> type) throws NoSuchFieldException, IllegalAccessException, SQLException;
}
