package io.github.kiryu1223.drink.base.toBean.handler.impl.list;

import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;
import io.github.kiryu1223.drink.base.util.DrinkUtil;

import java.lang.reflect.Type;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListStringHandler implements ITypeHandler<List<String>> {
    @Override
    public List<String> getValue(ResultSet resultSet, int index, Type type) throws SQLException {
        String string = resultSet.getString(index);
        if (DrinkUtil.isEmpty(string)) return Collections.emptyList();
        if (string.startsWith("[") && string.endsWith("]")) {
            string = string.substring(1, string.length() - 1);
        }
        return Arrays.stream(string.split(",")).collect(Collectors.toList());
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, List<String> value) throws SQLException {
        if (value == null) {
            preparedStatement.setNull(index, JDBCType.VARCHAR.getVendorTypeNumber());
        }
        else {
            preparedStatement.setString(index, "[" + String.join(",", value) + "]");
        }
    }
}
