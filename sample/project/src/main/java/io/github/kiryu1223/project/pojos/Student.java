package io.github.kiryu1223.project.pojos;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.Table;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@Table("students")
@FieldNameConstants
public class Student {
    @Column(primaryKey = true, generatedKey = true)
    private int id;
    private String name;
    private int age;
}
