package io.github.kiryu1223.app.test;

import io.github.kiryu1223.drink.api.client.DrinkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyService
{
    @Autowired
    DrinkClient drinkClient;

    public void show()
    {
        System.out.println(drinkClient);
    }
}
