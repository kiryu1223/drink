package io.github.kiryu1223.project;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.base.Aop;
import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.converter.SnakeNameConverter;
import io.github.kiryu1223.drink.base.util.DrinkUtil;
import io.github.kiryu1223.drink.core.SqlBuilder;
import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.project.pojos.Employee;
import io.github.kiryu1223.project.pojos.Student;

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
                .setDbType(DbType.PostgreSQL)
                // 名称转换风格
                .setNameConverter(new SnakeNameConverter())
                // 数据源
                .setDataSource(dataSource)
                .build();
    }

    public static void main(String[] args) {
        SqlClient client = boot();
        IConfig config = client.getConfig();
        Aop aop = config.getAop();
        aop.onInsert(Employee.class, e -> {
            String firstName = e.getFirstName();
            e.setFirstName(DrinkUtil.isEmpty(firstName) ? "a" : "b");
        });

        Student student = new Student();
        student.setName("老王");
        student.setAge(38);
        String sql = client.insertOrUpdate(student)
                .onConflict(s -> s.getId())
                .updateColumn(s -> s.getName())
                .updateColumn(s -> s.getAge())
                .updateColumn(s -> s.getId())
                .toSql();

        System.out.println(sql);
    }
}
