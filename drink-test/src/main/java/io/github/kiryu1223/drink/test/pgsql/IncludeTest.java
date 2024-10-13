package io.github.kiryu1223.drink.test.pgsql;

import io.github.kiryu1223.drink.pojos.Employee;
import io.github.kiryu1223.drink.pojos.Salary;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

public class IncludeTest extends BaseTest
{
    @Test
    public void oneManyTest()
    {
        Employee employee = client.query(Employee.class)
                .includes(e -> e.getSalaries())
                .first();

        Assert.assertEquals(17, employee.getSalaries().size());
    }

    @Test
    public void oneManyCondTest()
    {
        Employee employee = client.query(Employee.class)
                .includes(e -> e.getSalaries(), s -> s.getTo().isBefore(LocalDate.of(9999, 1, 1)))
                .first();

        Assert.assertEquals(16, employee.getSalaries().size());
    }

    @Test
    public void oneManyCond2Test()
    {
        List<Employee> employees = client.query(Employee.class)
                .includesByCond(e -> e.getSalaries(), query -> query
                        .orderBy(s -> s.getTo())
                        .where(s -> s.getTo().isBefore(LocalDate.of(9999, 1, 1)))
                )
                .limit(2)
                .toList();

        Employee e1 = employees.get(0);
        Assert.assertEquals(16, e1.getSalaries().size());

        Employee e2 = employees.get(1);
        Assert.assertEquals(5, e2.getSalaries().size());
    }

    @Test
    public void manyOneTest()
    {
        Salary salary = client.query(Salary.class)
                .include(e -> e.getEmployee())
                .first();

        Assert.assertEquals(10001, salary.getEmployee().getNumber());
    }

    @Test
    public void manyManyTest()
    {
        List<Employee> employees = client.query(Employee.class)
                .includesByCond(e -> e.getDepartments(), q -> q.limit(1))
                .limit(20)
                .toList();

        for (Employee employee : employees)
        {
            System.out.println(employee.getDepartments());
        }
    }

    @Test
    public void deepTest1()
    {
        Salary salary = client.query(Salary.class)
                .include(e -> e.getEmployee())
                .thenIncludes(s -> s.getSalaries())
                .first();

        Assert.assertEquals(17, salary.getEmployee().getSalaries().size());
    }
}
