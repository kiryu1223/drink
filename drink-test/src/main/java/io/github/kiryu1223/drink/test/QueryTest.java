package io.github.kiryu1223.drink.test;


import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.api.crud.read.LQuery;
import io.github.kiryu1223.drink.api.crud.read.group.GroupedQuery;
import io.github.kiryu1223.drink.api.crud.read.group.Grouper;
import io.github.kiryu1223.drink.ext.SqlFunctions;
import io.github.kiryu1223.drink.pojos.Employee;
import io.github.kiryu1223.drink.pojos.Gender;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

public class QueryTest extends BaseTest
{
    @Test
    public void q1()
    {
        List<? extends Result> list = client
                .query(Employee.class)
                .groupBy(g -> new Grouper()
                {
                    int a = g.getNumber();
                    String b = g.getFirstName();
                    LocalDate c = g.getHireDay();
                })
                .limit(50)
                .select(s -> new Result()
                {
                    int a = s.key.a;
                    String b = s.key.b;
                    LocalDate c = s.key.c;
                })
                .toList();

        for (Result result : list)
        {
            System.out.println(result);
        }
    }

    @Test
    public void q2()
    {
        LQuery<Employee> query = client.query(Employee.class);

        if(1==1)
        {
            query.where(e->e.getGender()!= Gender.F);
        }
        else
        {
            query.where(e->e.getGender()!= Gender.M);
        }

        for (Employee employee : query.toList())
        {
            System.out.println(employee);
        }
    }

    @Test
    public void q3()
    {

        String sql = client
                .query(Employee.class)
                .include(e -> e.getSalaries())
                .select().limit(50).toSql();
        System.out.println(sql);
//        List<Employee> list = client
//                .query(Employee.class)
//                .include(e -> e.getSalaries())
//                .limit(50)
//                .toList();
//
//
//        for (Employee employee : list)
//        {
//            System.out.println(employee);
//        }
    }
}
