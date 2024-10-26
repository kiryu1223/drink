package io.github.kiryu1223.app.pojos;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.Table;
import lombok.Data;

import java.time.LocalDate;

@Data
@Table("dept_manager")
public class DeptManager
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
