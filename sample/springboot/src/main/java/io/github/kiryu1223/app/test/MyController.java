package io.github.kiryu1223.app.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class MyController
{
    @Autowired
    MyService myService;

    @GetMapping("/test1")
    public void test1()
    {
        myService.show();
    }
}
