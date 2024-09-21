package io.github.kiryu1223.drink.test.mssql;

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
        Assert.assertEquals(16,e1.getSalaries().size());

        Employee e2 = employees.get(1);
        Assert.assertEquals(5,e2.getSalaries().size());
    }
}