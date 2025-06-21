package io.github.kiryu1223.project.pojos;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.Navigate;
import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.base.annotation.Table;
import io.github.kiryu1223.drink.core.api.ITable;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.util.List;

@Data
@Table("courses")
@FieldNameConstants
public class Course implements ITable
{
    @Column(primaryKey = true, generatedKey = true)
    private Integer id;
    private String title;
    private Integer creditHours;
    @Navigate(
            value = RelationType.ManyToMany,
            self = Fields.id,
            selfMapping = StudentCourse.Fields.couId,
            mappingTable = StudentCourse.class,
            targetMapping = StudentCourse.Fields.stuId,
            target = Students.Fields.id
    )
    private List<Students> students;
}
