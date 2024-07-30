package io.github.kiryu1223.drink.pojos;

import io.github.kiryu1223.drink.annotation.Column;
import io.github.kiryu1223.drink.annotation.IgnoreColumn;
import io.github.kiryu1223.drink.annotation.Table;
import io.github.kiryu1223.drink.converter.IntConverter;
import lombok.Data;

@Table("departments")
@Data
public class Department
{
    @Column("dept_no")
    private String number;
    @Column("dept_name")
    private String name;
}
