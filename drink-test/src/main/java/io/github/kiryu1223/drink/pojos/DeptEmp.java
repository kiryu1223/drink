package io.github.kiryu1223.drink.pojos;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.Table;
import io.github.kiryu1223.drink.base.metaData.IMappingTable;
import lombok.Data;

import java.time.LocalDate;

/**
 * 部门与员工多对多的中间表
 */
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
