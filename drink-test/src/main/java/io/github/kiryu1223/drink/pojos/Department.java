package io.github.kiryu1223.drink.pojos;

import io.github.kiryu1223.drink.annotation.*;
import lombok.Data;

import java.util.List;

/**
 * 部门
 */
@Data
@Table(value = "departments")
public class Department
{
    @Column("dept_no")
    private String number;
    @Column("dept_name")
    private String name;
    @Navigate(value = RelationType.OneToMany, self = "number", target = "deptNumber")
    private List<DeptManager> deptManager;
}
