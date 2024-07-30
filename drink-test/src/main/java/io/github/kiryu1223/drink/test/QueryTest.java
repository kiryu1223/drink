package io.github.kiryu1223.drink.test;


import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.api.crud.read.LQuery;
import io.github.kiryu1223.drink.api.crud.read.group.GroupedQuery;
import io.github.kiryu1223.drink.api.crud.read.group.Grouper;
import io.github.kiryu1223.drink.ext.SqlFunctions;
import io.github.kiryu1223.drink.pojos.DeptEmp;
import io.github.kiryu1223.drink.pojos.DeptManager;
import io.github.kiryu1223.drink.pojos.Employee;
import io.github.kiryu1223.drink.pojos.Gender;
import org.junit.Test;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        List<Employee> list = query.where(e -> e.getGender() != Gender.F).toList();

        System.out.println(list.size());
    }

    @Test
    public void q3()
    {

        List<Employee> list = client
                .query(Employee.class)
                //.include(e -> e.getSalaries())
                .limit(5)
                .toList();

        for (Employee employee : list)
        {
            //System.out.println(employee.getSalaries().size());
            System.out.println(employee);
        }
    }

    @Test
    public void q4()
    {
        // select e.emp_no from employees as e limit 1000

        List<Integer> list = client
                .query(Employee.class)
                .limit(1000)
                .endSelect(e -> e.getNumber())
                .toList();

        for (Integer employee : list)
        {
            System.out.println(employee);
        }
    }

    @Test
    public void q5()
    {
        Map<Integer, Employee> map = client
                .query(Employee.class)
                .includes(e -> e.getDepartments())
//                .includes(e -> e.getSalaries())
//                .includes(e -> e.getDeptEmp())
                .limit(5)
                .toMap(e -> e.getNumber());

        for (Map.Entry<Integer, Employee> entry : map.entrySet())
        {
            System.out.println(entry);
        }
    }

    @Test
    public void q6()
    {
        List<DeptManager> list = client
                .query(DeptManager.class)
                .include(dm -> dm.getDepartment())
                .limit(10)
                .toList();

        for (DeptManager deptManager : list)
        {
            System.out.println(deptManager);
        }
    }

    @Test
    public void q7()
    {
        List<Employee> list = client
                .query(Employee.class)
                .includes(e -> e.getSalaries(), s -> s.getTo().isBefore(LocalDate.of(9999, 1, 1)))
                //.includes(e -> e.getDepartments(), d -> d.getNumber() == "d005")
                .limit(5)
                .toList();

        for (Employee deptManager : list)
        {
            System.out.println(deptManager);
        }
    }
}
