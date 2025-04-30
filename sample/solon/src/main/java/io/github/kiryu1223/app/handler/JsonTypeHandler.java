package io.github.kiryu1223.app.handler;

import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;
import org.noear.snack.ONode;
import org.noear.solon.annotation.Component;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Component
public class JsonTypeHandler implements ITypeHandler<Map<String, Object>>
{
    @Override
    public Map<String, Object> getValue(ResultSet resultSet, int index, Type type) throws SQLException
    {
        return ONode.deserialize(resultSet.getString(index), Map.class);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, Map<String, Object> value) throws SQLException
    {
        preparedStatement.setString(index, ONode.serialize(value));
    }
}
