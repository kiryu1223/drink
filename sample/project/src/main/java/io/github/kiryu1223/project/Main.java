package io.github.kiryu1223.project;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.converter.SnakeNameConverter;
import io.github.kiryu1223.drink.base.page.DefaultPager;
import io.github.kiryu1223.drink.base.toBean.handler.JsonTypeHandler;
import io.github.kiryu1223.drink.base.toBean.handler.TypeHandlerManager;
import io.github.kiryu1223.drink.core.SqlBuilder;
import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.project.pojos.Course;
import io.github.kiryu1223.project.pojos.StudentCourse;
import io.github.kiryu1223.project.pojos.Students;

import java.lang.reflect.Type;
import java.util.List;

public class Main
{
    public static SqlClient boot()
    {
        TypeHandlerManager.setJsonTypeHandler(new JsonTypeHandler<Object>()
        {
            @Override
            public Object jsonToObject(String json, Type type)
            {
                return new Object();
            }

            @Override
            public String objectToJson(Object value)
            {
                return "{}";
            }
        });

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

    public static void main(String[] args)
    {
        SqlClient client = boot();
        IConfig config = client.getConfig();
//        Aop aop = config.getAop();
//        aop.onInsert(Employee.class, e -> {
//            String firstName = e.getFirstName();
//            e.setFirstName(DrinkUtil.isEmpty(firstName) ? "a" : "b");
//        });
//

        Course course = new Course();
        course.setId(1);
        String sql = client.insertOrUpdate(course)
                .toSql();

        System.out.println(sql);

//        String sql = client.query(Course.class)
//                .where(c -> c.getJson().get(3).getAaa() > 100)
//                .select(c->new Course(){
//                    {
//                        setStudents(
//                                client.query(Students.class)
//                                        .where(s->s.getId() == c.getId())
//                                        .toList()
//                        );
//                    }
//                    List<? extends Students> names= client.query(StudentCourse.class)
//                            .where(s->s.getCouId()==c.getId())
//                            .selectMany(s->s.getStudents())
//                            .toList();
//                })
//                .toSql();
//
//        System.out.println(sql);
    }
}
