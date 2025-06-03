package io.github.kiryu1223.drink.base.toBean.handler.impl.list;

import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;
import io.github.kiryu1223.drink.base.util.DrinkUtil;

import java.lang.reflect.Type;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListBoolHandler implements ITypeHandler<List<Boolean>> {
    @Override
    public List<Boolean> getValue(ResultSet resultSet, int index, Type type) throws SQLException {
        String string = resultSet.getString(index);
        if (DrinkUtil.isEmpty(string)) return Collections.emptyList();
        if (string.startsWith("[") && string.endsWith("]")) {
            string = string.substring(1, string.length() - 1);
        }
        return Arrays.stream(string.split(",")).map(s -> Boolean.parseBoolean(s)).collect(Collectors.toList());
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, List<Boolean> value) throws SQLException {
        if (value == null) {
            preparedStatement.setNull(index, JDBCType.VARCHAR.getVendorTypeNumber());
        }
        else {
            preparedStatement.setString(index, "[" + value.stream().map(i -> i.toString()).collect(Collectors.joining(",")) + "]");
        }
    }
}
