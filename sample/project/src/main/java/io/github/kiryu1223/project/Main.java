package io.github.kiryu1223.project;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.Filter;
import io.github.kiryu1223.drink.base.dataSource.DefaultDataSourceManager;
import io.github.kiryu1223.drink.core.Builder;
import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.project.pojos.Employee;
import io.github.kiryu1223.project.pojos.ITenant;

import java.time.LocalDate;
import java.util.Objects;

public class Main
{
    public static SqlClient boot()
    {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return Builder.bootStrap()
                .setDbType(DbType.MySQL)
                .setDataSourceManager(new DefaultDataSourceManager(dataSource))
                .build();
    }

    public static void main(String[] args)
    {
        SqlClient client = boot();

        Filter filter = client.getConfig().getFilter();

        filter.apply(
                ITenant.class,
                "1",
                z -> z.getTenantId() == 3
        );

        String list = client.query(Employee.class)
                .where(e -> e.query(e.getSalaries())
                        .any(s -> s.getSalary() < 39000
                        || Objects.equals(s.getFrom(), LocalDate.now()))
                )
                .toSql();

        System.out.println(list);


//        String list1 = client.query(Employee.class)
//                .where(e -> e.getNumber() == 10001)
//                .selectMany(e -> e.getSalaries())
//                .toSql();
//
//        System.out.println(list1);
    }
}
