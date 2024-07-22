package io.github.kiryu1223.drink.test;

import io.github.kiryu1223.drink.pojos.Department;
import io.github.kiryu1223.drink.pojos.DeptEmp;
import org.junit.Test;

@SuppressWarnings("all")
public class DeleteTest extends BaseTest
{
    @Test
    public void d1()
    {
        String sql = client.delete(Department.class)
                .where(w -> w.getNumber() == "10009")
                .toSql();
        System.out.println(sql);
    }

    @Test
    public void d2()
    {
        String sql = client.delete(Department.class)
                .leftJoin(DeptEmp.class, (d, dm) -> d.getNumber() == dm.getDeptNumber())
                .where((d, dm) -> d.getNumber() == "d009")
//                .selectDeleteTable((d, dm) -> d)
                .selectDelete((d, dm) -> dm)
                .toSql();
        System.out.println(sql);
    }
}
