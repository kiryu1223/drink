package io.github.kiryu1223.drink.pojos;

import io.github.kiryu1223.drink.annotation.Column;
import io.github.kiryu1223.drink.annotation.Navigate;
import io.github.kiryu1223.drink.annotation.RelationType;
import io.github.kiryu1223.drink.annotation.Table;
import lombok.Data;

import java.time.LocalDate;

@Data
@Table("dept_manager")
public class DeptManager
{
    @Column("emp_no")
    private int managerNumber;
    @Column("dept_no")
    private String deptNumber;
    @Column("from_date")
    private LocalDate from;
    @Column("to_date")
    private LocalDate to;
    @Navigate(value = RelationType.ManyToOne, self = "deptNumber", target = "number")
    private Department department;
}
