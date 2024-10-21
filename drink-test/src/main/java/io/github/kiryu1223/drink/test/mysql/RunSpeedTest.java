package io.github.kiryu1223.drink.test.mysql;

import io.github.kiryu1223.drink.pojos.Employee;
import io.github.kiryu1223.drink.pojos.Salary;
import org.junit.Test;

import java.util.List;

public class RunSpeedTest extends BaseTest
{
    @Test
    public void getEmployeeAll()
    {
        long start = System.currentTimeMillis();
        List<Employee> employees = client.query(Employee.class).toList();
        System.out.println("耗时" + (System.currentTimeMillis() - start) + "毫秒");
        System.out.println("返回" + employees.size() + "条数据");
    }

    @Test
    public void getSalaryAll()
    {
        long start = System.currentTimeMillis();
        List<Salary> salaries = client.query(Salary.class).toList();
        System.out.println("耗时" + (System.currentTimeMillis() - start) + "毫秒");
        System.out.println("返回" + salaries.size() + "条数据");
    }
}
