package io.github.kiryu1223.project;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.base.Aop;
import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.converter.SnakeNameConverter;
import io.github.kiryu1223.drink.base.page.DefaultPager;
import io.github.kiryu1223.drink.base.page.PagedResult;
import io.github.kiryu1223.drink.base.page.Pager;
import io.github.kiryu1223.drink.base.util.DrinkUtil;
import io.github.kiryu1223.drink.core.SqlBuilder;
import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.drink.core.api.ITable;
import io.github.kiryu1223.project.pojos.Employee;
import io.github.kiryu1223.project.pojos.Std;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static SqlClient boot() {
        //TypeHandlerManager.set(new GenderHandler());

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return SqlBuilder.bootStrap()
                // 数据库
                .setDbType(DbType.MySQL)
                // 名称转换风格
                .setNameConverter(new SnakeNameConverter())
                // 数据源
                .setDataSource(dataSource)
                .build();
    }

    static class PP extends DefaultPager {

    }

    public static void main(String[] args) {
        SqlClient client = boot();
        IConfig config = client.getConfig();
//        Aop aop = config.getAop();
//        aop.onInsert(Employee.class, e -> {
//            String firstName = e.getFirstName();
//            e.setFirstName(DrinkUtil.isEmpty(firstName) ? "a" : "b");
//        });
//
//        Std std = new Std();
//        std.setName("老王");
//        std.setAge(38);
//        String sql = client.insertOrUpdate(std)
//                .onConflict(s -> s.getId())
//                .updateColumn(s -> s.getName())
//                .updateColumn(s -> s.getAge())
//                .toSql();
//
//        System.out.println(sql);

        List<Employee> list = client.query(Employee.class)
                .as("emp")
                .where(e -> e.getNumber() == 10001)
                .ignoreColumn(e -> e.getNumber())
                .ignoreColumn(e -> e.getHireDay())
                .toList();

        System.out.println(list);
    }
}
