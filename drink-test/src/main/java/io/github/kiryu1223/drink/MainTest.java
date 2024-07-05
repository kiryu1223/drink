package io.github.kiryu1223.drink;

import io.github.kiryu1223.drink.api.Drink;
import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.api.crud.read.group.Grouper;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.MySQLConfig;
import io.github.kiryu1223.drink.pojos.Topic;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;

@SuppressWarnings("all")
public class MainTest
{
    public Logger log = LoggerFactory.getLogger(MainTest.class);
    public DrinkClient client;

    public MainTest()
    {
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
                .distinct()
                .selectSingle(r -> r.id0000)
                .toSql();
        System.out.println(sql);
//        log.info(sql);
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
                .selectSingle(s -> s.getStars())
                .toSql();
        System.out.println(sql);

//        log.info(sql);
    }

    @Test
    public void m3()
    {
        String sql = client.query(Topic.class)
                .leftJoin(Topic.class, (a, b) -> a.getId() == b.getId())
                .where((a, b) -> a.getStars() >= 1000 || b.getTitle() != "123")
                .orderBy((a, b) -> a.getId(), false)
                .orderBy((a, b) -> b.getCreateTime(), true)
                .distinct()
                .select((a, b) -> new Result()
                {
                    int id0000 = b.getStars();
                    String stars0000 = a.getId();
                })
                .selectSingle(s -> s.id0000)
                .toSql();
        System.out.println(sql);

//        log.info(sql);
    }

    @Test
    public void m4()
    {
        String sql = client.query(Topic.class)
                .leftJoin(Topic.class, (a, b) -> a.getId() == b.getId())
                .where((a, b) -> a.getStars() >= 1000 || b.getTitle() != "123")
                .groupBy((a, b) -> new Grouper()
                {
                    int k1 = a.getStars();
                    String k2 = a.getId();
                })
                .orderBy((a) -> a.key.k2, false)
                .orderBy((a) -> a.key.k1, true)
                .having(a -> a.key.k1 > 500)
                .select((a) -> new Result()
                {
                    int id0000 = a.key.k1;
                    String stars0000 = a.key.k2;
                })
                .toSql();
        System.out.println(sql);

//        log.info(sql);
    }

    @Test
    public void m5()
    {
        String sql = client.query(Topic.class)
                .leftJoin(Topic.class, (a, b) -> a.getId() == b.getId())
                .where((a, b) -> a.getStars() >= 1000 || b.getTitle() != "123")
                .groupBy((a, b) -> a.getStars())
                .orderBy((a) -> a.key, false)
                .having(a -> a.key > 500 && a.count(0) != 50)
                .select(a -> new Result()
                {
                    int a00 = a.key;
                    BigDecimal b00 = a.sum((s, b) -> s.getStars());
                })
                .toSql();
        System.out.println(sql);

//        log.info(sql);
    }

    @Test
    public void m6()
    {
        String sql = client.query(Topic.class)
                .leftJoin(Topic.class, (a, b) -> a.getId() == b.getId())
                .where((a, b) -> a.getStars() >= 1000 || b.getTitle() != "123")
                .selectSingle((a, b) -> b)
                .toSql();
        System.out.println(sql);

//        log.info(sql);
    }

    @Test
    public void m7()
    {
        String sql = client.query(Topic.class)
                .leftJoin(Topic.class, (a, b) -> a.getId() == b.getId())
                .where((a, b) -> a.getStars() >= 1000 || b.getTitle() != "123")
                .groupBy((a, b) -> new Grouper()
                {
                    int k1 = a.getStars();
                    String k2 = a.getId();
                })
                .orderBy((a) -> a.key.k2, false)
                .orderBy((a) -> a.key.k1, true)
                .toSql();

        System.out.println(sql);
    }

    @Test
    public void m8()
    {
        String sql = client.query(Topic.class)
                .leftJoin(Topic.class, (a, b) -> a.getId() == b.getId())
                .where((a, b) -> a.getStars() >= 1000 || b.getTitle() != "123")
                .groupBy((a, b) -> a.getStars())
                .orderBy((a) -> a.key, false)
                .toSql();

        System.out.println(sql);
    }

    @Test
    public void m9()
    {
        String sql = client.query(Topic.class).toSql();

        System.out.println(sql);
    }

}