package io.github.kiryu1223.drink.oldtest;

import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.ext.SqlFunctions;
import io.github.kiryu1223.drink.pojos.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;



public class DepartmentTest extends BaseTest
{
    private final DrinkClient client;

    public DepartmentTest()
    {
        client = mysql;
    }
    @Test
    public void q1()
    {
        long start = System.currentTimeMillis();
        List<DeptManager> list = client.query(DeptManager.class).toList();
        System.out.println(System.currentTimeMillis() - start);
        for (DeptManager department : list)
        {
            System.out.println(department);
        }
        //System.out.println(client.query(Department.class).toSql());

        long start2 = System.currentTimeMillis();
        List<DeptManager> list2 = client.query(DeptManager.class).toList();
        System.out.println(System.currentTimeMillis() - start2);
        for (DeptManager department : list2)
        {
            System.out.println(department);
        }
    }

    @Test
    public void q2()
    {
        List<Department> list = client.query(Department.class).limit(5).toList();
        for (Department employee : list)
        {
            System.out.println(employee);
        }
    }

    @Test
    public void q3()
    {
        List<Employee> list1 = client.query(Employee.class).limit(10).toList();


        long start = System.currentTimeMillis();
        List<? extends Result> list = client.query(Salary.class)
                .limit(100)
                .select(s -> new Result()
                {
                    int num = s.getEmpNumber();
                    int money = s.getSalary();
                    LocalDate on = s.getFrom();
                    LocalDate off = s.getTo();
                })
                .toList();

        System.out.println("耗时: " + (System.currentTimeMillis() - start));
        System.out.println(list.size());
    }

    @Test
    public void q4()
    {
        List<Long> list1 = client.query(Employee.class).endSelect(s -> SqlFunctions.count()).toList();
        System.out.println(list1.get(0));
    }


}
