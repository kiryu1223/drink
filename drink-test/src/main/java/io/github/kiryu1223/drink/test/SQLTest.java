package io.github.kiryu1223.drink.test;

import io.github.kiryu1223.drink.Drink;
import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.api.crud.read.group.Grouper;
import io.github.kiryu1223.drink.api.transaction.DefaultTransactionManager;
import io.github.kiryu1223.drink.api.transaction.TransactionManager;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.dataSource.DefaultDataSourceManager;
import io.github.kiryu1223.drink.core.session.DefaultSqlSessionFactory;
import io.github.kiryu1223.drink.core.session.SqlSessionFactory;
import io.github.kiryu1223.drink.ext.SqlFunctions;
import io.github.kiryu1223.drink.ext.SqlTimeUnit;
import io.github.kiryu1223.drink.pojos.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static io.github.kiryu1223.drink.ext.SqlCalculates.between;
import static io.github.kiryu1223.drink.ext.SqlFunctions.addDate;

@SuppressWarnings("all")
public class SQLTest
{
    public Logger log = LoggerFactory.getLogger(SQLTest.class);
    public DrinkClient client;

    public SQLTest()
    {
        DataSourceManager dataSourceManager = new DefaultDataSourceManager(new DataSource()
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
        });
        TransactionManager transactionManager = new DefaultTransactionManager(dataSourceManager);
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(dataSourceManager, transactionManager);
        client = Drink.bootStrap()
                .setDataSourceManager(dataSourceManager)
                .setTransactionManager(transactionManager)
                .setSqlSessionFactory(sqlSessionFactory)
                .build();
    }

    @Test
    public void m0()
    {
        String sql = client.query(Topic.class).toSql();
        log.info(sql);
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
                .endSelect(r -> r.id0000)
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
                .endSelect(s -> s.getStars())
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
                .endSelect(s -> s.id0000)
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
                .having(a -> a.key > 500 && a.count((c1, c2) -> c2.getCreateTime()) != 50)
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
                .select(Top.class)
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
        String sql = client.query(Topic.class)
                .where(a -> SqlFunctions.convert(a.getId(), int.class) > 50)
                .endSelect(s -> SqlFunctions.count(s.getId()))
                .toSql();

        System.out.println(sql);
    }

    @Test
    public void m10()
    {
        String sql = client.query(Topic.class)
                .where(a -> SqlFunctions.convert(a.getId(), int.class) > 50)
                .endSelect(s -> SqlFunctions.groupJoin("-", s.getId(), s.getTitle()))
                .toSql();

        System.out.println(sql);
    }

    @Test
    public void m11()
    {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        String sql1 = client.query(Topic.class)
                .where(a -> list.contains(a.getStars()))
                .endSelect(s -> SqlFunctions.groupJoin("-", s.getId(), s.getTitle()))
                .toSql();

        System.out.println(sql1);

        String sql2 = client.query(Topic.class)
                .where(a -> "aabb".contains(a.getTitle()) || "aabb".startsWith(a.getTitle()) || "aabb".endsWith(a.getTitle()))
                .endSelect(s -> SqlFunctions.groupJoin("-", s.getId(), s.getTitle()))
                .toSql();
        System.out.println(sql2);
    }

    @Test
    public void m12()
    {
        String sql1 = client.query(Topic.class)
                .endSelect(s -> addDate(s.getCreateTime(), SqlTimeUnit.DAY, 500))
                .toSql();

        System.out.println(sql1);
    }

    @Test
    public void m13()
    {
        String sql1 = client.query(Topic.class)
                .where(w -> between(w.getStars(), 0, 500))
                .endSelect(s -> s)
                .toSql();

        System.out.println(sql1);
    }

    @Test
    public void m14()
    {
        //exists(client.query(Top.class).where(t1 -> t1.getTitle() == t0.getTitle()).endSelect(s -> 1))
        String sql1 = client.query(Topic.class)
                .leftJoin(Topic.class, (a, b) -> a.getId() == b.getId())
                .exists(Top.class, (a, b, c) -> a.getTitle() == SqlFunctions.asciiToStr(c.getStars()))
                .exists(Top.class, (a, b, c) -> b.getTitle() == SqlFunctions.asciiToStr(c.getStars()))
                .endSelect((s1, s2) -> 1)
                .toSql();

        System.out.println(sql1);
    }

    @Test
    public void m15()
    {
        String sql1 = client.query(Topic.class)
                .exists(Top.class, (a, b) -> a.getId() == b.getTitle())
                .endSelect((s1) -> 1)
                .toSql();

        System.out.println(sql1);
    }

    @Test
    public void m16()
    {
        String sql1 = client.query(Topic.class)
                .where(w -> w.getId() == "0")
                .where(w -> w.getId() == "5")
                .orWhere(w -> w.getId() == "9")
                .toSql();

        System.out.println(sql1);
    }

    @Test
    public void m17()
    {
        String sql1 = client.query(Topic.class)
                .notExists(Top.class, (a, b) -> a.getId() != b.getTitle())
                .exists(Top.class, (a, b) -> a.getStars() >= b.getStars())
                .toSql();

        System.out.println(sql1);
    }

    @Test
    public void m18()
    {
        GenderConverter genderConverter = new GenderConverter();
        System.out.println(genderConverter.getDbType());
        System.out.println(genderConverter.getJavaType());
    }

    @Test
    public void m19()
    {
        LocalDate end = LocalDate.of(9999, 1, 1);
        String sql = client.query(DeptEmp.class)
                .innerJoin(Salary.class, (de, s) -> de.getEmpNumber() == s.getEmpNumber())
                .innerJoin(Department.class, (de, s, d) -> de.getDeptNumber() == d.getNumber())
                .where((de, s, d) -> de.getDeptNumber() == "d001" && s.getTo() == end)
                .groupBy((de, s, d) -> new Grouper()
                {
                    String id = de.getDeptNumber();
                    String name = d.getName();
                })
                .select(g -> new Result()
                {
                    String deptId = g.key.id;
                    String deptName = g.key.name;
                    BigDecimal avgSalary = g.avg((de, s, d) -> s.getSalary());
                }).toSql();

        System.out.println(sql);
    }

    //@Test
    public void m20()
    {
        List<? extends Result> list = client.query(UserDo.class)
                .leftJoin(UserAddress.class, (u, ua) -> u.getId() == ua.getId())
                .leftJoin(Area.class, (u, ua, a) -> ua.getId() == a.getId())
                .where((u, ua, a) ->
                        u.getId() == 1 && ua.getTel().contains("10086")
                )
                .select((u, ua, a) -> new Result()
                {
                    int id = u.getId();
                    String name = u.getName();
                    int sex = u.getSex();
                    String url = u.getHeadImg();
                    String tel = ua.getTel();
                    String address = ua.getAddress();
                    String pro = a.getProvince();
                    String ct = a.getCity();
                })
                .toList();

        List<UserDo> list3 = client.query(UserDo.class)
                .include(u -> u.getAddress())
                .include(u -> u.getArea())
                .where(u -> u.getId() == 1)
                .toList();

    }
}