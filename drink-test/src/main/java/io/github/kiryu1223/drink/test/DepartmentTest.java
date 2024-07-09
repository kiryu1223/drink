package io.github.kiryu1223.drink.test;

import io.github.kiryu1223.drink.annotation.Column;
import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.ext.SqlFunctions;
import io.github.kiryu1223.drink.pojos.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;


public class DepartmentTest extends BaseTest
{


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
        List<? extends Result> list = client.query(Employee.class).limit(50)
                .select(s -> new Result()
                {
                    int num = s.getNumber();
                    String firstName = s.getFirstName();
                    LocalDate on = s.getBirthDay();
                    LocalDate off = s.getHireDay();
                    //@Column(converter = GenderConverter.class)
                    Gender gg = s.getGender();
                })
                .toList();
        System.out.println("耗时: " + (System.currentTimeMillis() - start));
        for (Result result : list)
        {
            System.out.println(result);
        }
    }

    @Test
    public void q4()
    {
        List<Long> list1 = client.query(Employee.class).selectSingle(s -> SqlFunctions.count()).toList();
        System.out.println(list1.get(0));
    }
}
