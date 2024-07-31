package io.github.kiryu1223.drink.pojos;

import io.github.kiryu1223.drink.annotation.*;
import io.github.kiryu1223.drink.converter.IntConverter;
import lombok.Data;

import java.util.List;

@Table("departments")
@Data
public class Department
{
    @Column("dept_no")
    private String number;
    @Column("dept_name")
    private String name;
    @Navigate(value = RelationType.OneToMany, self = "number", target = "deptNumber")
    private List<DeptManager> deptManager;
}
