package io.github.kiryu1223.drink;

import io.github.kiryu1223.drink.api.Drink;
import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.MySQLConfig;
import io.github.kiryu1223.drink.pojos.Topic;
import io.github.kiryu1223.expressionTree.delegate.Action0;
import io.github.kiryu1223.expressionTree.delegate.Action1;
import org.junit.Test;

import javax.sql.DataSource;
import javax.swing.*;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

@SuppressWarnings("all")
public class MainTest
{
    @Test
    public void m1()
    {
        Config config = new Config();
        config.setDbConfig(new MySQLConfig());
        DrinkClient client = Drink.boot(new DataSource()
                {
                    @Override
                    public Connection getConnection() throws SQLException
                    {
                        return null;
                    }

                    @Override
                    public Connection getConnection(String username, String password) throws SQLException
                    {
                        return null;
                    }

                    @Override
                    public <T> T unwrap(Class<T> iface) throws SQLException
                    {
                        return null;
                    }

                    @Override
                    public boolean isWrapperFor(Class<?> iface) throws SQLException
                    {
                        return false;
                    }

                    @Override
                    public PrintWriter getLogWriter() throws SQLException
                    {
                        return null;
                    }

                    @Override
                    public void setLogWriter(PrintWriter out) throws SQLException
                    {

                    }

                    @Override
                    public void setLoginTimeout(int seconds) throws SQLException
                    {

                    }

                    @Override
                    public int getLoginTimeout() throws SQLException
                    {
                        return 0;
                    }

                    @Override
                    public Logger getParentLogger() throws SQLFeatureNotSupportedException
                    {
                        return null;
                    }
                })
                .setConfig(config)
                .build();
        String sql = client.query(Topic.class)
                .where(a -> a.getStars() >= 1000)
                .where(b -> b.getTitle() != "123")
                .orderBy(f -> f.getId())
                .orderBy(f -> f.getCreateTime(), false)
                .select(s -> new Result()
                {
                    int id = s.getStars();
                    String star = s.getId();
                })
                .toSql();
        System.out.println(sql);
    }
}