package io.github.kiryu1223.drink.pojos;

import io.github.kiryu1223.drink.annotation.Column;
import io.github.kiryu1223.drink.annotation.Table;
import io.github.kiryu1223.drink.ext.IMappingTable;
import lombok.Data;

import java.time.LocalDate;

@Data
@Table("dept_emp")
public class DeptEmp implements IMappingTable
{
    @Column("emp_no")
    private int empNumber;
    @Column("dept_no")
    private String deptNumber;
    @Column("from_date")
    private LocalDate from;
    @Column("to_date")
    private LocalDate to;
}
