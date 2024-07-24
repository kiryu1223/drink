package io.github.kiryu1223.drink.pojos;

import io.github.kiryu1223.drink.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
@Table("employees")
public class Employee
{
    @Column("emp_no")
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

    @IgnoreColumn
    @Navigate(value = RelationType.OneToMany,self = "number",target = "empNumber")
    private List<Salary> salaries;
}
