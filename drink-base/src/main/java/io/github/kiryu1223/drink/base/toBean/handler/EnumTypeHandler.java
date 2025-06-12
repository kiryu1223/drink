package io.github.kiryu1223.drink.base.toBean.handler;

import io.github.kiryu1223.drink.base.exception.DrinkException;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumTypeHandler<T extends Enum<T>> implements ITypeHandler<T> {

    public static final EnumTypeHandler<?> Instance = new EnumTypeHandler<>();
    @Override
    public T getValue(ResultSet resultSet, int index, Type type) throws SQLException {
        return Enum.valueOf((Class<T>) type, resultSet.getString(index));
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, T value) throws SQLException {
        preparedStatement.setString(index, value.name());
    }

    @Override
    public final Type getActualType() {
        throw new DrinkException(String.format("不能把%s用于类型处理器",getClass().getName()));
    }
}
