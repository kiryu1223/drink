package io.github.kiryu1223.drink.pojos;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.Navigate;
import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.base.annotation.Table;
import io.github.kiryu1223.drink.core.api.ITable;
import lombok.Data;

import java.time.LocalDate;

/**
 * 薪水
 */
@Data
@Table(value = "salaries")
public class Salary implements ITable
{
    @Column(value = "emp_no",primaryKey = true)
    private int empNumber;
    private int salary;
    @Column("from_date")
    private LocalDate from;
    @Column("to_date")
    private LocalDate to;

    @Navigate(value = RelationType.ManyToOne,self = "empNumber",target = "number")
    private Employee employee;
}
