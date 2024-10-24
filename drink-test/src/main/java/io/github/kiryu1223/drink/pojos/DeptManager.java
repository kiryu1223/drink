package io.github.kiryu1223.drink.pojos;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.Navigate;
import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.base.annotation.Table;
import lombok.Data;

import java.time.LocalDate;

/**
 * 部门管理者
 */
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
