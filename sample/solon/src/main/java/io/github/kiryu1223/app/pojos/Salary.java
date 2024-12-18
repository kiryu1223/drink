package io.github.kiryu1223.app.pojos;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.Table;
import lombok.Data;

import java.time.LocalDate;

@Data
@Table("salaries")
public class Salary
{
    @Column("emp_no")
    private int empNumber;
    private int salary;
    @Column("from_date")
    private LocalDate from;
    @Column("to_date")
    private LocalDate to;
}
