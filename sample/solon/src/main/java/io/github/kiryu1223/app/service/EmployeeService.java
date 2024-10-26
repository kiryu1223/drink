package io.github.kiryu1223.app.service;

import io.github.kiryu1223.app.pojos.*;
import io.github.kiryu1223.drink.base.sqlExt.SqlTimeUnit;
import io.github.kiryu1223.drink.core.api.Result;
import io.github.kiryu1223.drink.core.api.client.DrinkClient;
import io.github.kiryu1223.drink.core.api.crud.read.group.Grouper;
import io.github.kiryu1223.drink.core.sqlExt.SqlFunctions;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class EmployeeService
{
    @Inject
    DrinkClient client;

    // 查询员工的基本信息和当前职位

    // SELECT e.emp_no, e.first_name, e.last_name, e.gender, e.birth_date,
    //       de.dept_no, d.dept_name,
    //       t.title, t.from_date, t.to_date
    // FROM employees e
    // LEFT JOIN titles t ON e.emp_no = t.emp_no
    // LEFT JOIN dept_emp de ON e.emp_no = de.emp_no AND de.to_date = '9999-01-01'
    // LEFT JOIN departments d ON de.dept_no = d.dept_no
    // WHERE e.emp_no = ${empId};
    // LIMIT 1

    public Result getEmployeeDataByEmployeeNumber(int empId)
    {
        return client.query(Employee.class)
                .leftJoin(Titles.class, (a, b) -> a.getNumber() == b.getEmpNumber())
                .leftJoin(DeptEmp.class, (a, b, c) ->
                        a.getNumber() == c.getEmpNumber()
                                && c.getTo() == LocalDate.of(9999, 1, 1)
                )
                .leftJoin(Department.class, (a, b, c, d) -> c.getDeptNumber() == d.getNumber())
                .where((a, b, c, d) -> a.getNumber() == empId)
                .select((a, b, c, d) -> new Result()
                {
                    int empId = a.getNumber();
                    String fristName = a.getFirstName();
                    String lastName = a.getLastName();
                    Gender gender = a.getGender();
                    LocalDate birth = a.getBirthDay();
                    String deptId = d.getNumber();
                    String deptName = d.getName();
                    String title = b.getTitle();
                    LocalDate from = b.getFrom();
                    LocalDate to = b.getTo();
                }).first();

    }

    // 查询某部门的所有员工及其当前职位

    // SELECT e.emp_no, e.first_name, e.last_name, e.gender, e.birth_date,
    // t.title, t.from_date AS title_start_date, t.to_date AS title_end_date
    // FROM employees e
    // JOIN dept_emp de ON e.emp_no = de.emp_no
    // JOIN titles t ON e.emp_no = t.emp_no
    // WHERE de.dept_no = ${DepartmentId} AND de.to_date = '9999-01-01';

    public List<? extends Result> findEmployeesDataByDepartmentNumber(String DepartmentId)
    {
        LocalDate endTime = LocalDate.of(9999, 1, 1);

        return client.query(Employee.class)
                .innerJoin(DeptEmp.class, (e, de) -> e.getNumber() == de.getEmpNumber())
                .innerJoin(Titles.class, (e, de, t) -> e.getNumber() == t.getEmpNumber())
                .where((e, de, t) -> de.getDeptNumber() == DepartmentId && de.getTo() == endTime)
                .select((e, de, t) -> new Result()
                {
                    int empId = e.getNumber();
                    String fristName = e.getFirstName();
                    String lastName = e.getLastName();
                    Gender gender = e.getGender();
                    LocalDate birth = e.getBirthDay();
                    String title = t.getTitle();
                    LocalDate from = t.getFrom();
                    LocalDate to = t.getTo();
                })
                .toList();
    }

    // 查询某员工的薪水历史记录

    // SELECT s.emp_no, e.first_name, e.last_name, s.salary, s.from_date, s.to_date
    // FROM salaries s
    // JOIN employees e ON s.emp_no = e.emp_no
    // WHERE s.emp_no = ${empId}
    // ORDER BY s.from_date DESC;

    public List<? extends Result> findEmployeeSalaryHistoryByEmployeeNumber(int empId)
    {
        List<? extends Result> list = client.query(Salary.class)
                .innerJoin(Employee.class, (s, e) -> s.getEmpNumber() == e.getNumber())
                .where((s, e) -> s.getEmpNumber() == empId)
                .orderBy((s, e) -> s.getFrom(), false)
                .select((s, e) -> new Result()
                {
                    int empId = s.getEmpNumber();
                    String fristName = e.getFirstName();
                    String lastName = e.getLastName();
                    int sal = s.getSalary();
                    LocalDate from = s.getFrom();
                    LocalDate to = s.getTo();
                })
                .toList();

        return list;
    }

    // 查询某部门的当前负责人及其任职时间

    // SELECT dm.emp_no, e.first_name, e.last_name, dm.from_date AS manager_start_date, dm.to_date AS manager_end_date
    // FROM dept_manager dm
    // JOIN employees e ON dm.emp_no = e.emp_no
    // WHERE dm.dept_no = ${departmentId} AND dm.to_date = '9999-01-01';
    // LIMIT 1

    public Result getDeptManagerEmployeeByDepartmentNumber(String departmentId)
    {
        return client.query(DeptManager.class)
                .innerJoin(Employee.class, (dm, e) -> dm.getEmpNumber() == e.getNumber())
                .where((dm, e) -> dm.getDeptNumber() == departmentId && dm.getTo() == LocalDate.of(9999, 1, 1))
                .select((dm, e) -> new Result()
                {
                    int managerId = dm.getEmpNumber();
                    String fristName = e.getFirstName();
                    String lastName = e.getLastName();
                    LocalDate from = dm.getFrom();
                    LocalDate to = dm.getTo();
                }).first();
    }

    // 查询员工在公司的总工作年限

    // SELECT emp_no, DATEDIFF(CURDATE(), hire_date) AS total_days_worked,
    // FLOOR(DATEDIFF(CURDATE(), hire_date) / 365) AS total_years_worked
    // FROM employees
    // WHERE emp_no = 10001;
    // LIMIT 1

    public Result getEmployeeWorkedTimeByEmployeeId(int empId)
    {
        return client.query(Employee.class)
                .where(e -> e.getNumber() == empId)
                .select(e -> new Result()
                {
                    int id = e.getNumber();
                    long totalDaysWorked = SqlFunctions.dateTimeDiff(SqlTimeUnit.DAY, e.getHireDay(),SqlFunctions.nowDate());
                    long totalYearsWorked =SqlFunctions.dateTimeDiff(SqlTimeUnit.YEAR, e.getHireDay(),SqlFunctions.nowDate());
                })
                .first();
    }

    // 查询某部门员工的平均薪水

    // SELECT de.dept_no, d.dept_name, AVG(s.salary) AS avg_salary
    // FROM dept_emp de
    // JOIN salaries s ON de.emp_no = s.emp_no
    // JOIN departments d ON de.dept_no = d.dept_no
    // WHERE de.dept_no = ${departmentId} AND s.to_date = '9999-01-01'
    // GROUP BY de.dept_no, d.dept_name;

    public Result getAverageSalaryByDepartmentId(String departmentId)
    {
        LocalDate end = LocalDate.of(9999, 1, 1);

        return client.query(DeptEmp.class)
                .innerJoin(Salary.class, (de, s) -> de.getEmpNumber() == s.getEmpNumber())
                .innerJoin(Department.class, (de, s, d) -> de.getDeptNumber() == d.getNumber())
                .where((de, s, d) -> de.getDeptNumber() == departmentId && s.getTo() == end)
                .groupBy((de, s, d) -> new Grouper()
                {
                    String id = de.getDeptNumber();
                    String name = d.getName();
                })
                .select(g -> new Result()
                {
                    String deptId = g.key.id;
                    String deptName = g.key.name;
                    BigDecimal avgSalary = g.avg((de, s, d) -> s.getSalary());
                })
                .first();
    }

    public List<User> abc()
    {
        return client.query(User.class)
                .where(u -> u.getUsername() != null)
                .limit(100)
                .toList();
    }
}
