package io.github.kiryu1223.app.controller;

import io.github.kiryu1223.app.service.MyService;
import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;

import javax.sql.DataSource;
import java.util.List;

@Controller
public class MyController
{
    @Inject
    MyService myService;

//    @Inject("client2")
//    DrinkClient client2;
//
//    @Inject("client3")
//    DrinkClient client3;
//
//    @Inject("dynamicClient")
//    DrinkClient dynamicClient;

    @Mapping("/test1")
    public long test1()
    {
        return myService.showCount();
    }

    @Mapping("/test2")
    public List<? extends Result> test2()
    {
        return myService.showList();
    }

    @Mapping("/test3")
    public long test3()
    {
        return myService.insertTest();
    }

    @Mapping("/test4")
    public long test4()
    {
        return myService.dataBaseTest();
    }
}
