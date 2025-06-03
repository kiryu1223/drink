package io.github.kiryu1223.project;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.Filter;
import io.github.kiryu1223.drink.base.converter.SnakeNameConverter;
import io.github.kiryu1223.drink.base.sqlExt.Over;
import io.github.kiryu1223.drink.base.sqlExt.Rows;
import io.github.kiryu1223.drink.base.toBean.beancreator.AbsBeanCreator;
import io.github.kiryu1223.drink.base.toBean.handler.TypeHandlerManager;
import io.github.kiryu1223.drink.core.SqlBuilder;
import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.drink.core.api.Result;
import io.github.kiryu1223.drink.func.SqlFunctions;
import io.github.kiryu1223.project.handler.GenderHandler;
import io.github.kiryu1223.project.pojos.Course;
import io.github.kiryu1223.project.pojos.Employee;
import io.github.kiryu1223.project.pojos.Salary;
import io.github.kiryu1223.project.pojos.Student;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static SqlClient boot() {
        TypeHandlerManager.set(new GenderHandler());

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

    public static void main(String[] args) {
        SqlClient client = boot();

        AbsBeanCreator<Employee> creator = client.getConfig()
                .getBeanCreatorFactory()
                .get(Employee.class);

        creator.setBeanCreator(()->new Employee());
        creator.setBeanSetter("number",(a, b)->a.setNumber((int)b));
        creator.setBeanGetter("number",(a)->a.getNumber());

        Filter filter = client.getConfig().getFilter();


//        List<? extends Employee> list = client.query(Employee.class)
//                .where(e -> e.getNumber() < 10020)
//                .select(s -> new Employee() {
//                    // select
//                    {
//                        // select count(*) from courses as c where c.id = s.id,
//                        setNumber(client.query(Employee.class).count());
//                    }
//                    // (select new {...} from courses as c where c.id = s.id) as `c4`
//                    List<? extends Employee> c4 = client.query(Employee.class)
//                            .where(c -> c.getNumber() == s.getNumber())
//                            .toList();
//                })
//                .toList();

//        String sql = client.query(Student.class)
//                .where(e -> client.query(Course.class)
//                        .any(c -> c.getCourseId() == e.getStudentId() && c.getCourseName()=="A003")
//                )
//                .toSql();

//        for (Employee employee : list) {
//            System.out.println(employee.toString(client.getConfig()));
//        }
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
        String sql2 = client.query(Course.class)
                .includeMany(c -> c.getStudents(), then -> then.where(s -> s.getMajor().equals("计算机科学与技术")))
                .selectAggregate(a -> new Result() {
                    long id = SqlFunctions.rawSql("(select c.id from courses as c)");
                    long number2 = a.over(
                            Over.partitionBy(
                                    a.table.getCourseId(),
                                    a.table.getCourseName(),
                                    a.table.getCredit()
                            ),
                            Over.orderBy(a.table.getClassroom()),
                            Over.between(Rows.first(),Rows.last())
                    ).rowNumber();
                })
                .toSql();

        System.out.println(sql2);
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
