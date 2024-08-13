package io.github.kiryu1223.drink.test;


import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.api.crud.read.LQuery;
import io.github.kiryu1223.drink.api.crud.read.group.Grouper;
import io.github.kiryu1223.drink.pojos.*;
import org.junit.Test;

import java.time.LocalDate;
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
        LocalDate end = LocalDate.of(9999, 1, 1);

        List<Employee> list = client
                .query(Employee.class)
                .includesByCond(e -> e.getSalaries(), q -> q
                        .where(s -> s.getTo().isBefore(end))
                        .orderBy(s -> s.getSalary())
                        .limit(5)
                )
                .limit(5)
                .toList();

        for (Employee employee : list)
        {
            System.out.println(employee.getSalaries().size());
            System.out.println(employee);
        }
    }

    @Test
    public void q3_1()
    {
        LocalDate end = LocalDate.of(9999, 1, 1);

        List<Employee> list = client
                .query(Employee.class)
                .includes(e -> e.getSalaries(), s -> s.getTo().isBefore(end))
                .limit(5)
                .toList();

        for (Employee employee : list)
        {
            System.out.println(employee.getSalaries().size());
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
                .include(dm -> dm.getDepartment(), dm -> dm.getName().startsWith("Mark"))
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
        LocalDate end = LocalDate.of(9999, 1, 1);

        List<Employee> list = client
                .query(Employee.class)
                .includesByCond(e -> e.getSalaries(), cond -> cond.where(s -> s.getTo().isBefore(end)))
                .limit(5)
                .toList();

        for (Employee deptManager : list)
        {
            System.out.println(deptManager);
        }
    }

    @Test
    public void q8()
    {
        List<Employee> list = client
                .query(Employee.class)
                .includesByCond(e -> e.getDepartments(),
                        query -> query.limit(1)
                )
//                .thenIncludes(d -> d.getDeptManager())
//                .thenInclude(dm -> dm.getDepartment())
                .limit(100)
                .toList();

        for (Employee deptManager : list)
        {
            System.out.println(deptManager);
        }
    }

    @Test
    public void oneToManyTest()
    {
        LocalDate end = LocalDate.of(9999, 1, 1);

        List<Employee> list1 = client
                .query(Employee.class)
                .includes(e -> e.getSalaries(), s -> s.getTo().isBefore(end))
                .limit(100)
                .toList();

        for (Employee deptManager : list1)
        {
            System.out.println(deptManager);
        }

        List<Employee> list2 = client
                .query(Employee.class)
                .includesByCond(e -> e.getSalaries(), q -> q
                        .where(s -> s.getTo().isBefore(end))
                        .limit(5, 5)
                        .orderBy(s -> s.getFrom())
                        .orderBy(s -> s.getSalary())
                )
                .limit(10)
                .toList();

        for (Employee deptManager : list2)
        {
            //System.out.println(deptManager.getSalaries().size());
            System.out.println(deptManager);
        }
    }

    @Test
    public void q10()
    {
        LocalDate end = LocalDate.of(9999, 1, 1);

        List<Employee> list = client
                .query(Employee.class)
                .limit(10)
                .includes(e -> e.getSalaries(), s -> s.getTo().isBefore(end))
                .thenInclude(s -> s.getEmployee())
                .toList();

        for (Employee deptManager : list)
        {
            System.out.println();
            deptManager.getSalaries().forEach(System.out::println);
        }
    }

    @Test
    public void q11()
    {
        // LocalDate end = LocalDate.of(9999, 1, 1);

        List<Employee> list = client
                .query(Employee.class)
                .limit(10)
                .includesByCond(e -> e.getDepartments(),q -> q.limit(1))
                .thenIncludesByCond(d -> d.getDeptManager(), q -> q.limit(1))
                .toList();

        for (Employee deptManager : list)
        {
            System.out.println(deptManager);
        }
    }

    @Test
    public void q12()
    {
        // LocalDate end = LocalDate.of(9999, 1, 1);

        List<Department> list = client
                .query(Department.class)
                .limit(10)
                .includes(e -> e.getDeptManager())
                .toList();

        for (Department department : list)
        {
            System.out.println(department);
        }
    }
}
