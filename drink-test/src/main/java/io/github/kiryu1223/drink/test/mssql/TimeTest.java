package io.github.kiryu1223.drink.test.mssql;

import io.github.kiryu1223.drink.pojos.Employee;
import org.junit.Test;

public class TimeTest extends BaseTest
{
    @Test
    public void t1()
    {
        //long start = System.nanoTime();
        for (int i = 1; i <= 100; i++)
        {
            client.query(Employee.class).where(e -> e.getNumber() == 10100).first();
//            long end = System.nanoTime();
//            System.out.printf("第%d次 耗时%d纳秒 %n", i, end - start);
//            start = System.nanoTime();
        }
    }
}
