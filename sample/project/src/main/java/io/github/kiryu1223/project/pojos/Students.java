package io.github.kiryu1223.project.pojos;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.Table;
import io.github.kiryu1223.drink.core.api.ITable;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@Table("students")
@FieldNameConstants
public class Students implements ITable
{
    @Column(primaryKey = true, generatedKey = true)
    private Integer id;
    private String name;
    private String email;
}
