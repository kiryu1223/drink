package io.github.kiryu1223.app;


import io.github.kiryu1223.app.service.MyService;
import io.github.kiryu1223.drink.starter.DrinkAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class SpringApp implements ApplicationListener<ApplicationStartedEvent>
{
    @Autowired
    MyService myService;

    public static void main(String[] args)
    {
        SpringApplication.run(SpringApp.class, args);
    }


    @Override
    public void onApplicationEvent(ApplicationStartedEvent event)
    {
        myService.dsTest();

    }
}
