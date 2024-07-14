package io.github.kiryu1223.app.service;

import io.github.kiryu1223.app.pojos.Employee;
import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.ext.SqlFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MyService
{
    @Autowired
    DrinkClient client;

    public long showCount()
    {
        long aLong = client
                .query(Employee.class)
                .selectSingle(s -> SqlFunctions.count())
                .toList()
                .get(0);

        System.out.println(aLong);
        return aLong;
    }

    public List<? extends Result> showList()
    {
        List<? extends Result> list = client
                .query(Employee.class)
                .limit(50)
                .select(s -> new Result()
                {
                    int a = s.getNumber();
                    String b = s.getFirstName();
                    LocalDate c = s.getHireDay();
                })
                .toList();

        System.out.println(list.size());
        return list;
    }
}
