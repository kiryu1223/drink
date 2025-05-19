package io.github.kiryu1223.project;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.Filter;
import io.github.kiryu1223.drink.base.converter.SnakeNameConverter;
import io.github.kiryu1223.drink.base.dataSource.DefaultDataSourceManager;
import io.github.kiryu1223.drink.base.sqlExt.Rows;
import io.github.kiryu1223.drink.base.toBean.handler.TypeHandlerManager;
import io.github.kiryu1223.drink.core.Builder;
import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.drink.core.api.Result;
import io.github.kiryu1223.drink.func.SqlFunctions;
import io.github.kiryu1223.project.handler.GenderHandler;
import io.github.kiryu1223.project.pojos.Course;
import io.github.kiryu1223.project.pojos.Student;

import java.util.EnumSet;
import java.util.List;

import static io.github.kiryu1223.drink.base.sqlExt.Rows.first;
import static io.github.kiryu1223.drink.base.sqlExt.Rows.last;
import static io.github.kiryu1223.drink.func.SqlFunctions.*;

public class Main {
    public static SqlClient boot() {
        TypeHandlerManager.set(new GenderHandler());

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return Builder.bootStrap()
                .setDbType(DbType.MySQL)
                .setNameConverter(new SnakeNameConverter())
                .setDataSourceManager(new DefaultDataSourceManager(dataSource))
                .build();
    }

    public static void main(String[] args) {
        SqlClient client = boot();

        Filter filter = client.getConfig().getFilter();


//        List<Student> list = client.query(Student.class)
//                .where(s -> s.query(s.getCourses())
//                        .any(c -> c.getCourseName()=="A003")
//                )
//                .toList();

//        String sql = client.query(Student.class)
//                .where(e -> client.query(Course.class)
//                        .any(c -> c.getCourseId() == e.getStudentId() && c.getCourseName()=="A003")
//                )
//                .toSql();
//
//        System.out.println(sql);
//
//        for (Student student : list)
//        {
//            System.out.println(student);
//        }
//
//        List<Student> list1 = client.query(Course.class)
//                .where(c -> c.getCourseName().equals("深度学习"))
//                .selectMany(c -> c.getStudents())
//                .toList();
//
//        for (Student student : list1)
//        {
//            System.out.println(student);
//        }
//
        String sql = client.query(Course.class)
                .select(c -> new Result() {
                    int id = c.getCourseId();
                    long number = SqlFunctions.over(c.getCourseId()).rowNumber();
                })
                .toSql();

        System.out.println(sql);

//        List<Course> list2 = client.query(Course.class)
//                .selectAggregate(Course.class, (c, s) ->
//                {
//                    s.setCourseName(c.value1.getCourseName());
//                    s.setCredit(c.value1.getCredit());
//                    s.setTeacher(c.value1.getTeacher());
//                    s.setTop5(c.value1.query(c.value1.getStudents())
//                            .groupBy(cc -> new Grouper()
//                            {
//                                String name = cc.getName();
//                            })
//                            .select(g -> g.join(g.key.name))
//                            .first());
//                })
//                .toList();
//
//        for (Course course : list2)
//        {
//            System.out.println(course);
//        }

//        List<? extends Result> list2 = client.query(Student.class)
//                .select(s -> new Result()
//                {
//                    int id = s.getStudentId();
//                    String name = s.getName();
//                    String mj = s.getMajor();
//                })
//                .toList();
//
//        for (Result result : list2)
//        {
//            System.out.println(result);
//        }


//        filter.apply(
//                ITenant.class,
//                "1",
//                z -> z.getTenantId() == 3
//        );
//
//        String list = fSql.query(Employee.class)
//                .where(e -> e.query(e.getSalaries())
//                        .any(s -> s.getSalary() < 39000)
//                )
//                .toSql();
//
//        System.out.println(list);
//
//        String sql = fSql.query(Employee.class)
//                .where(e -> fSql.query(Salary.class)
//                        .any(s -> s.getEmpNumber() == e.getNumber() && s.getSalary() < 39000)
//                )
//                .toSql();
//
//        System.out.println(sql);


//        String list1 = client.query(Employee.class)
//                .where(e -> e.getNumber() == 10001)
//                .selectMany(e -> e.getSalaries())
//                .toSql();
//
//        System.out.println(list1);
    }
}
