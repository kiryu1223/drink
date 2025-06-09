package io.github.kiryu1223.project;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.converter.SnakeNameConverter;
import io.github.kiryu1223.drink.base.session.SqlSession;
import io.github.kiryu1223.drink.base.toBean.handler.TypeHandlerManager;
import io.github.kiryu1223.drink.core.SqlBuilder;
import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.drink.core.api.crud.read.pivot.Pivoted;
import io.github.kiryu1223.drink.core.api.crud.read.pivot.UnPivoted;
import io.github.kiryu1223.project.handler.GenderHandler;
import io.github.kiryu1223.project.pojos.QuarterSales;
import io.github.kiryu1223.project.pojos.SalesByQuarter;

import java.util.Arrays;

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
                .setDbType(DbType.SQLServer)
                // 名称转换风格
                .setNameConverter(new SnakeNameConverter())
                // 数据源
                .setDataSource(dataSource)
                .build();
    }

    public static void main(String[] args) {
        SqlClient client = boot();
        SqlSession session = client.getConfig().getSqlSessionFactory().getSession();


//        AbsBeanCreator<Salary> creator = client.getConfig()
//                .getBeanCreatorFactory()
//                .get(Salary.class);

//        creator.setBeanCreator(() -> new Salary());
//        creator.setBeanSetter("empNumber", (s, v) -> s.setEmpNumber((int) v));
//        creator.setBeanSetter("salary", (s, v) -> s.setSalary((int) v));
//        creator.setBeanSetter("from", (s, v) -> s.setFrom((LocalDate) v));
//        creator.setBeanSetter("to", (s, v) -> s.setTo((LocalDate) v));

//        TransPair<String> one = TransPair.of("qar1", "一季度");
//        TransPair<String> two = TransPair.of("qar2", "二季度");
//        TransPair<String> three = TransPair.of("qar3", "三季度");
//        TransPair<String> four = TransPair.of("qar4", "四季度");

//        String sql = client.query(SalesByQuarter.class)
//                .as("sq")
//                .pivot(
//                        // 选择需要转换的值列以及对他们的聚合操作
//                        s -> s.sum(x -> x.getAmount()),
//                        // 选择需要转换的名称列
//                        s -> s.getQuarter(),
//                        // 生成出的新地列名称
//                        Arrays.asList("一季度", "二季度", "三季度", "四季度"),
//                        // 选取剩余需要的列(可空)
//                        s -> new Pivoted<String, Integer>() {
//                            int year2 = s.getYear();
//                        }
//                )
//                //.where(s -> s.year == 2021)
//                .select(s -> new QuarterSales() {
//                    {
//                        setYear(s.year2);
//                        setQuarter1(s.column("一季度"));
//                        setQuarter2(s.column("二季度"));
//                        setQuarter3(s.column("三季度"));
//                        setQuarter4(s.column("四季度"));
//                    }
//                })
//                .toSql();
//
//        System.out.println(sql);

        String sql2 = client.query(QuarterSales.class)
                .as("qs")
                .unPivot(
                        q -> new UnPivoted<Integer>() {
                            int year = q.getYear();
                        },
                        q -> q.getQuarter1(),
                        q -> q.getQuarter2(),
                        q -> q.getQuarter3(),
                        q -> q.getQuarter4()
                )
                .select(q -> new SalesByQuarter() {
                    {
                        setYear(q.year);
                        setQuarter(q.nameColumn());
                        setAmount(q.valueColumn());
                    }
                })
                .toSql();

        System.out.println(sql2);
//        long start = System.currentTimeMillis();
//        List<Salary> first = client.query(Salary.class).toList();
//        System.out.println(first.size());
//        System.out.println("冷启动查询耗时:" + (System.currentTimeMillis() - start));
//
//
//        long start2 = System.currentTimeMillis();
//        List<Salary> second = client.query(Salary.class).toList();
//        System.out.println(second.size());
//        System.out.println("第一次查询耗时:" + (System.currentTimeMillis() - start2));


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
//        String sql2 = client.query(Course.class)
//                .includeMany(c -> c.getStudents(), then -> then.where(s -> s.getMajor().equals("计算机科学与技术")))
//                .selectAggregate(a -> new Result() {
//                    long id = SqlFunctions.rawSql("(select c.id from courses as c)");
//                    long number2 = a.over(
//                            Over.partitionBy(
//                                    a.table.getCourseId(),
//                                    a.table.getCourseName(),
//                                    a.table.getCredit()
//                            ),
//                            Over.orderBy(a.table.getClassroom()),
//                            Over.between(Rows.first(),Rows.last())
//                    ).rowNumber();
//                })
//                .toSql();
//
//        System.out.println(sql2);
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
