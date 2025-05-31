package io.github.kiryu1223.project.pojos;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.Table;
import io.github.kiryu1223.drink.core.api.ITable;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Data
@Table("salaries")
@FieldNameConstants
public class Salary implements ITable
{
    @Column("emp_no")
    private int empNumber;
    private int salary;
    @Column("from_date")
    private LocalDate from;
    @Column("to_date")
    private LocalDate to;
}
