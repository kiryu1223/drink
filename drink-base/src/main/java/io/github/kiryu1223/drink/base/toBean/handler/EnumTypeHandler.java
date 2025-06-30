package io.github.kiryu1223.drink.base.toBean.handler;

import io.github.kiryu1223.drink.base.util.DrinkUtil;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class EnumTypeHandler<T extends Enum<T>> implements ITypeHandler<T> {

    public static final EnumTypeHandler<?> Instance = new EnumTypeHandler() {
        Void v;

        @Override
        public Class<?> getActualType() {
            return Enum.class;
        }
    };

    @Override
    public T getValue(ResultSet resultSet, int index, Type type) throws SQLException {
        String string = resultSet.getString(index);
        if (!DrinkUtil.isEmpty(string)) {
            return Enum.valueOf((Class<T>) type, string);
        }
        return null;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, T value) throws SQLException {
        preparedStatement.setString(index, value.name());
    }

    @Override
    public abstract Class<T> getActualType();
}
