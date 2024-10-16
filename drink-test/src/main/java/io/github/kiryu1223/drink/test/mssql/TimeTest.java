package io.github.kiryu1223.drink.test.mssql;

import io.github.kiryu1223.drink.annotation.Column;
import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.api.StopWatch;
import io.github.kiryu1223.drink.pojos.Employee;
import io.github.kiryu1223.drink.pojos.Gender;
import io.github.kiryu1223.drink.pojos.GenderConverter;
import org.junit.Test;

import java.time.LocalDate;

public class TimeTest extends BaseTest
{
    @Test
    public void t1()
    {
        for (int i = 1; i <= 100; i++)
        {
            //StopWatch.start();
            client.query(Employee.class).where(e -> e.getNumber() == 10100)
                    .select(e -> new Result()
                    {
                        int number=e.getNumber();
                        LocalDate birthDay=e.getBirthDay();
                        String firstName=e.getFirstName();
                        String lastName=e.getLastName();
                        Gender gender=e.getGender();
                        LocalDate hireDay=e.getHireDay();
                    }).first();
        }
    }
}
