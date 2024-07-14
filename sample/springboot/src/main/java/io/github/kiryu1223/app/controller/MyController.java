package io.github.kiryu1223.app.controller;

import io.github.kiryu1223.app.service.MyService;
import io.github.kiryu1223.drink.api.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app")
public class MyController
{
    @Autowired
    MyService myService;

    @GetMapping("/test1")
    public long test1()
    {
        return myService.showCount();
    }

    @GetMapping("/test2")
    public List<? extends Result> test2()
    {
        return myService.showList();
    }
}
