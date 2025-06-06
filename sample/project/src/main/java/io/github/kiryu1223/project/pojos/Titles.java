package io.github.kiryu1223.project.pojos;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.Table;
import lombok.Data;

import java.time.LocalDate;

@Data
@Table("titles")
public class Titles
{
    @Column("emp_no")
    private int empNumber;
    private String title;
    @Column("from_date")
    private LocalDate from;
    @Column("to_date")
    private LocalDate to;
}
