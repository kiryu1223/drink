package io.github.kiryu1223.project.pojos;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.Table;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@Table("stds")
@FieldNameConstants
public class Std
{
    @Column(primaryKey = true, generatedKey = true)
    private int id;
    private String name;
    private int age;
}
