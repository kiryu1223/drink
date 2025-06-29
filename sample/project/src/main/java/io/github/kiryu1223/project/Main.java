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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


        Map<String, Integer> map = new HashMap<>();
        map.put("小王",1);
        map.put("小张",2);
        map.put("小明",3);

        client.query(Students.class)
                .where(s->map.getOrDefault(s.getName(),2)==2);
    }
}
