package io.github.kiryu1223.drink.test;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DBConnectionTest
{
    private static final Logger log = LoggerFactory.getLogger(DBConnectionTest.class);

    //@Test
    public void sqliteTest() throws SQLException
    {
        try (HikariDataSource dataSource = new HikariDataSource())
        {
            dataSource.setDriverClassName("org.sqlite.JDBC");
            dataSource.setJdbcUrl("jdbc:sqlite:D:/sqlite3/test.db");
            try (Connection connection = dataSource.getConnection())
            {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT DATE('now','localtime')"))
                {
                    try (ResultSet resultSet = preparedStatement.executeQuery())
                    {
                        resultSet.next();
                        LocalDate now = resultSet.getObject(1, LocalDate.class);
                        log.info(now.toString());
                    }
                }
            }
        }
    }

    @Test
    public void pgsqlTest() throws SQLException
    {
        try (HikariDataSource dataSource = new HikariDataSource())
        {
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/");
            dataSource.setUsername("postgres");
            dataSource.setPassword("root");
            try (Connection connection = dataSource.getConnection())
            {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT AGE('2000-10-27'::TIMESTAMP, '1996-10-20'::TIMESTAMP)"))
                {
                    try (ResultSet resultSet = preparedStatement.executeQuery())
                    {
                        resultSet.next();
                        Object object = resultSet.getObject(1);
                        log.info(String.valueOf(object.getClass()));
                        log.info(String.valueOf(object));
                    }
                }
            }
        }
    }
}
