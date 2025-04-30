package io.github.kiryu1223.drink.pojos;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.Navigate;
import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.base.annotation.Table;
import lombok.Data;

import java.util.List;

/**
 * 部门
 */
//@Data
@Table(value = "departments")
public class Department
{
    @Column("dept_no")
    private String number;
    @Column("dept_name")
    private String name;
    @Navigate(value = RelationType.OneToMany, self = "number", target = "deptNumber")
    private List<DeptManager> deptManager;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
