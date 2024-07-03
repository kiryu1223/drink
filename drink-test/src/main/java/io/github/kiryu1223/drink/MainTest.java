package io.github.kiryu1223.drink;

import io.github.kiryu1223.drink.api.Drink;
import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.MySQLConfig;
import io.github.kiryu1223.drink.pojos.Topic;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

@SuppressWarnings("all")
public class MainTest
{
    public Logger log = LoggerFactory.getLogger(MainTest.class);
    public DrinkClient client;

    public MainTest()
    {
        log.info("123321");
        Config config = new Config();
        config.setDbConfig(new MySQLConfig());
        client = Drink.boot(new DataSource()
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
                    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException
                    {
                        return null;
                    }
                })
                .setConfig(config)
                .build();
    }

    @Test
    public void m1()
    {
        String sql = client.query(Topic.class)
                .where(a -> a.getStars() >= 1000)
                .where(b -> b.getTitle() != "123")
                .orderBy(f -> f.getId())
                .orderBy(f -> f.getCreateTime(), false)
                .distinct()
                .select(s -> new Result()
                {
                    int id0000 = s.getStars();
                    String stars0000 = s.getId();
                })
                .toSql();
        System.out.println(sql);
    }

    @Test
    public void m2()
    {
        String sql = client.query(Topic.class)
                .where(a -> a.getStars() >= 1000)
                .where(b -> b.getTitle() != "123")
                .orderBy(f -> f.getId())
                .orderBy(f -> f.getCreateTime(), false)
                .distinct()
                .selectInt(s -> s.getStars())
                .toSql();
        System.out.println(sql);
    }
}