package io.github.kiryu1223.app;


import io.github.kiryu1223.drink.starter.DrinkAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(DrinkAutoConfiguration.class)
public class App
{
    public static void main(String[] args)
    {
        SpringApplication.run(App.class, args);
    }
}
