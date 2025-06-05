package io.github.kiryu1223.project.pojos;

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
    private int courseId;
    private String courseName;
    private BigDecimal credit;
    private String department;
    private String teacher;
    private String classroom;
    @Navigate(
            value = RelationType.ManyToMany,
            self = Fields.courseId,
            selfMapping = StudentCourse.Fields.courseId,
            mappingTable = StudentCourse.class,
            targetMapping = StudentCourse.Fields.studentId,
            target = Student.Fields.studentId
    )
    private List<Student> students;
}
