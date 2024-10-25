package io.github.kiryu1223.app.controller;

import io.github.kiryu1223.app.service.EmployeeService;
import io.github.kiryu1223.app.service.MyService;
import io.github.kiryu1223.drink.core.api.Result;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Path;

import java.util.List;

@Controller
public class MyController
{
    @Inject
    MyService myService;

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

    @Inject
    EmployeeService employeeService;

    @Mapping("/employeeData/{empId}")
    public Result employeeData(@Path int empId)
    {
        return employeeService.getEmployeeDataByEmployeeNumber(empId);
    }

    @Mapping("/findEmployee/{departmentId}")
    public List<? extends Result> findEmployee(@Path String departmentId)
    {
        return employeeService.findEmployeesDataByDepartmentNumber(departmentId);
    }


    @Mapping("/employeeSalary/{empId}")
    public List<? extends Result> employeeSalary(@Path int empId)
    {
        return employeeService.findEmployeeSalaryHistoryByEmployeeNumber(empId);
    }

    @Mapping("/deptManagerEmployee/{departmentId}")
    public Result deptManagerEmployee(@Path String departmentId)
    {
        return employeeService.getDeptManagerEmployeeByDepartmentNumber(departmentId);
    }

    @Mapping("/employeeWorkedTime/{empId}")
    public Result employeeWorkedTime(@Path int empId)
    {
        return employeeService.getEmployeeWorkedTimeByEmployeeId(empId);
    }

    @Mapping("/averageSalary/{departmentId}")
    public Result averageSalary(@Path String departmentId)
    {
        return employeeService.getAverageSalaryByDepartmentId(departmentId);
    }
}
