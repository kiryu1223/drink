package io.github.kiryu1223.project.pojos;

import io.github.kiryu1223.drink.base.annotation.*;
import io.github.kiryu1223.drink.core.api.ITable;
import io.github.kiryu1223.project.handler.GenderHandler;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.util.List;


@Data
@Table("employees")
@FieldNameConstants
public class Employee implements ITable, ITenant
{
    @Column("emp_no")
    private int number;
    @Column("birth_date")
    private LocalDate birthDay;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    private Gender gender;
    @Column("hire_date")
    private LocalDate hireDay;
    @Navigate(
            value = RelationType.OneToMany,
            self = Fields.number,
            target = Salary.Fields.empNumber
    )
    private List<Salary> salaries;

    private int tenantId;
}
