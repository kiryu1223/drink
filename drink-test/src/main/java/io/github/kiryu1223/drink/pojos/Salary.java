package io.github.kiryu1223.drink.pojos;

import io.github.kiryu1223.drink.annotation.Column;
import io.github.kiryu1223.drink.annotation.Navigate;
import io.github.kiryu1223.drink.annotation.RelationType;
import io.github.kiryu1223.drink.annotation.Table;
import lombok.Data;

import java.time.LocalDate;

/**
 * 薪水
 */
@Data
@Table(value = "salaries")
public class Salary
{
    @Column("emp_no")
    private int empNumber;
    private int salary;
    @Column("from_date")
    private LocalDate from;
    @Column("to_date")
    private LocalDate to;

    @Navigate(value = RelationType.ManyToOne,self = "empNumber",target = "number")
    private Employee employee;
}
