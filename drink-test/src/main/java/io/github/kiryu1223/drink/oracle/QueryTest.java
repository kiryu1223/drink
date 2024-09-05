package io.github.kiryu1223.drink.oracle;

import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.api.crud.read.group.Grouper;
import io.github.kiryu1223.drink.pojos.Department;
import io.github.kiryu1223.drink.pojos.DeptEmp;
import io.github.kiryu1223.drink.pojos.Salary;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class QueryTest extends OracleTest
{
    private static final Logger log = LoggerFactory.getLogger(QueryTest.class);

    @Test
    public void q1()
    {
        LocalDate end = LocalDate.of(9999, 1, 1);

        List<? extends Result> list = client.query(DeptEmp.class)
                .innerJoin(Salary.class, (de, s) -> de.getEmpNumber() == s.getEmpNumber())
                .innerJoin(Department.class, (de, s, d) -> de.getDeptNumber() == d.getNumber())
                .where((de, s, d) -> de.getDeptNumber() == "d001" && s.getTo() == end)
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
                }).toList();

        log.info(list.toString());
    }
}
