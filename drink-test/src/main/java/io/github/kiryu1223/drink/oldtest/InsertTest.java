package io.github.kiryu1223.drink.oldtest;

import io.github.kiryu1223.drink.core.api.client.DrinkClient;
import io.github.kiryu1223.drink.base.transaction.Transaction;
import io.github.kiryu1223.drink.pojos.Department;
import org.junit.Test;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class InsertTest extends BaseTest
{private final DrinkClient client;

    public InsertTest()
    {
        client = mysql;
    }
    @Test
    public void i1() throws InterruptedException
    {
        client.useDs("2024");

        try (Transaction transaction = client.beginTransaction())
        {
            Department department1 = new Department();
            department1.setNumber("100");
            //department1.setName("LLL");
            Department department2 = new Department();
            department2.setNumber("200");
            //department2.setName("MMM");
            Department department3 = new Department();
            department3.setNumber("300");
            //department3.setName("SSS");
            long execute = client
                    .insert(Arrays.asList(department1, department2, department3))
                    .executeRows();

            Thread thread = new Thread(() ->
            {
                try (Transaction transaction2 = client.beginTransaction(Connection.TRANSACTION_REPEATABLE_READ))
                {
                    List<Department> list = client.query(Department.class).toList();
                    for (Department department : list)
                    {
                        System.out.println(department);
                    }
                }
            });
            thread.start();

            Thread.sleep(1000);
            transaction.rollback();
        }
    }


}
