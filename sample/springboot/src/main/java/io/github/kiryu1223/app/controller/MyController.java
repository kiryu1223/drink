package io.github.kiryu1223.app.controller;

import io.github.kiryu1223.app.service.EmployeeService;
import io.github.kiryu1223.app.service.MyService;
import io.github.kiryu1223.drink.api.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/test3")
    public long test3()
    {
        return myService.insertTest();
    }

    @GetMapping("/test4")
    public long test4()
    {
        return myService.dataBaseTest();
    }

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/employeeData/{empId}")
    public Result employeeData(@PathVariable int empId)
    {
        return employeeService.getEmployeeDataByEmployeeNumber(empId);
    }

    @GetMapping("/findEmployee/{departmentId}")
    public List<? extends Result> findEmployee(@PathVariable String departmentId)
    {
        return employeeService.findEmployeesDataByDepartmentNumber(departmentId);
    }


    @GetMapping("/employeeSalary/{empId}")
    public List<? extends Result> employeeSalary(@PathVariable int empId)
    {
        return employeeService.findEmployeeSalaryHistoryByEmployeeNumber(empId);
    }

    @GetMapping("/deptManagerEmployee/{departmentId}")
    public Result deptManagerEmployee(@PathVariable String departmentId)
    {
        return employeeService.getDeptManagerEmployeeByDepartmentNumber(departmentId);
    }

    @GetMapping("/employeeWorkedTime/{empId}")
    public Result employeeWorkedTime(@PathVariable int empId)
    {
        return employeeService.getEmployeeWorkedTimeByEmployeeId(empId);
    }

    @GetMapping("/averageSalary/{departmentId}")
    public Result averageSalary(@PathVariable String departmentId)
    {
        return employeeService.getAverageSalaryByDepartmentId(departmentId);
    }
}
