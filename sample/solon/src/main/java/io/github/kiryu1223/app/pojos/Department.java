package io.github.kiryu1223.app.pojos;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.Table;
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
