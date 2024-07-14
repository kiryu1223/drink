package io.github.kiryu1223.drink.test;

import io.github.kiryu1223.drink.pojos.Department;
import org.junit.Test;

@SuppressWarnings("all")
public class DeleteTest extends BaseTest
{
    @Test
    public void d1()
    {
        String sql = client.delete(Department.class)
                .where(w -> w.getNumber().equals("100"))
                .toSql();
        System.out.println(sql);
    }
}
