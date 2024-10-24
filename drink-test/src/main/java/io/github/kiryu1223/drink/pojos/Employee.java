package io.github.kiryu1223.drink.pojos;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.Navigate;
import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.base.annotation.Table;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工
 */
@Data
@Table("employees")
public class Employee
{
    @Column(value = "emp_no",primaryKey = true)
    private int number;
    @Column("birth_date")
    private LocalDate birthDay;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    @Column(converter = GenderConverter.class)
    private Gender gender;
    @Column("hire_date")
    private LocalDate hireDay;
    @Navigate(value = RelationType.OneToMany, self = "number", target = "empNumber")
    private List<Salary> salaries;
    @Navigate(value = RelationType.OneToMany, self = "number", target = "empNumber")
    private List<DeptEmp> deptEmp;

    @Navigate(
            value = RelationType.ManyToMany,
            self = "number",
            target = "number",
            mappingTable = DeptEmp.class,
            selfMapping = "empNumber",
            targetMapping = "deptNumber"
    )
    private List<Department> departments;
}
