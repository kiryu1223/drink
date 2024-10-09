package io.github.kiryu1223.drink.test;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnectionTest
{
    @Test
    public void sqliteTest() throws SQLException
    {
        try (HikariDataSource dataSource = new HikariDataSource())
        {
            dataSource.setDriverClassName("org.sqlite.JDBC");
            dataSource.setJdbcUrl("jdbc:sqlite:D:/sqlite3/test.db");
            try (Connection connection = dataSource.getConnection())
            {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT CAST(STRFTIME('%Y','2020-10-27') AS INTEGER)"))
                {
                    try (ResultSet resultSet = preparedStatement.executeQuery())
                    {
                        resultSet.next();
                        Object object = resultSet.getObject(1);
                        System.out.println(object);
                        System.out.println(object.getClass());
                    }
                }
            }
        }
    }
}
