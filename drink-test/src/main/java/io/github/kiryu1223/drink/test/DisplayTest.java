package io.github.kiryu1223.drink.test;

import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.api.crud.read.group.Grouper;
import io.github.kiryu1223.drink.ext.SqlFunctions;
import io.github.kiryu1223.drink.pojos.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DisplayTest extends BaseTest
{
    @Test
    public void d1()
    {
        int id = 10001;
        String sql = client.query(Employee.class)
                .where(e -> e.getNumber() == id)
                .toSql();
        System.out.println(sql);
    }

    @Test
    public void d2()
    {
        String sql = client.query(Employee.class)
                .where(e -> e.getGender() == Gender.F && e.getFirstName() == "lady")
                .toSql();
        System.out.println(sql);
    }

    @Test
    public void d3()
    {
        int id = 10001;
        String list = client.query(Employee.class)
                .leftJoin(Salary.class, (e, s) -> e.getNumber() == s.getEmpNumber())
                .where((e, s) -> e.getNumber() == id)
                .select((e, s) -> new Result()
                {
                    String name = SqlFunctions.concat(e.getFirstName(), " ", e.getLastName());
                    int maxSalary = SqlFunctions.max(s.getSalary());
                    BigDecimal avgSalary = SqlFunctions.avg(s.getSalary());
                })
                .toSql();

        System.out.println(list);
    }

    @Test
    public void d4()
    {
        String departmentId = "d009";

        String list = client.query(DeptEmp.class)
                .innerJoin(Salary.class, (de, s) -> de.getEmpNumber() == s.getEmpNumber())
                .innerJoin(Department.class, (de, s, d) -> de.getDeptNumber() == d.getNumber())
                .where((de, s, d) -> de.getDeptNumber() == departmentId && s.getTo() == LocalDate.of(9999, 1, 1))
                .groupBy((de, s, d) -> new Grouper()
                {
                    String id = de.getDeptNumber();
                    String name = d.getName();
                })
                .select(g -> new Result()
                {
                    String deptId = g.key.id;
                    String deptName = g.key.name;
                    BigDecimal avgSalary = g.avg((de, s, d) -> s.getSalary());
                })
                .toSql();

        System.out.println(list);
    }
}
