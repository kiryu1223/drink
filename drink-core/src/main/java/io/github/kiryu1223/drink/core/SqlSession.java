package io.github.kiryu1223.drink.core;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

class SqlSession
{
    public interface Function<T, R>
    {
        R invoke(T t) throws SQLException;
    }

    private final DataSource dataSource;

    public SqlSession()
    {
        this.dataSource = DataSourcesManager.getDataSource();
    }

    public SqlSession(String key)
    {
        this.dataSource = DataSourcesManager.getDataSource(key);
    }

    public <R> R executeQuery(Function<ResultSet, R> func, String sql, List<Objects> values)
    {
        try (Connection connection = dataSource.getConnection())
        {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
            {
                setObjects(preparedStatement, values);
                try (ResultSet resultSet = preparedStatement.executeQuery())
                {
                    return func.invoke(resultSet);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void setObjects(PreparedStatement preparedStatement, List<Objects> values) throws SQLException
    {
        for (int i = 1; i <= values.size(); i++)
        {
            Objects value = values.get(i);
            preparedStatement.setObject(i, value);
        }
    }
}
