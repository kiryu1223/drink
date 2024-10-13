package io.github.kiryu1223.drink.test.pgsql;

import io.github.kiryu1223.drink.pojos.Employee;
import io.github.kiryu1223.drink.pojos.Salary;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LimitTest extends BaseTest
{
    private static final Logger log = LoggerFactory.getLogger(LimitTest.class);

    @Test
    public void firstTest()
    {
        Employee first = client.query(Employee.class).first();
        log.info(first.toString());
    }

    @Test
    public void limitHasPrimaryKeyTest()
    {
        Employee first = client.query(Employee.class).limit(1).first();
        log.info(first.toString());

        List<Employee> list1 = client.query(Employee.class).limit(1).toList();
        log.info(list1.toString());

        List<Employee> list2 = client.query(Employee.class).limit(5, 5).toList();
        log.info(list2.toString());
    }

    @Test
    public void limitHasOrderByTest()
    {
        List<Employee> list1 = client.query(Employee.class)
                .orderBy(e -> e.getBirthDay())
                .limit(5, 5)
                .toList();
        log.info(list1.toString());

        List<Salary> list2 = client.query(Salary.class)
                .orderBy(s -> s.getSalary())
                .limit(5, 5)
                .toList();
        log.info(list2.toString());
    }
}
