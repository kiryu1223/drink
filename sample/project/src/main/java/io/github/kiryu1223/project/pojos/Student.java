package io.github.kiryu1223.project.pojos;

import io.github.kiryu1223.drink.base.annotation.Navigate;
import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.base.annotation.Table;
import io.github.kiryu1223.drink.core.util.List;
import io.github.kiryu1223.drink.core.api.ITable;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Data
@Table("students")
@FieldNameConstants
public class Student implements ITable
{
    private int studentId;
    private String name;
    private Gender gender;
    private LocalDate birthDate;
    private LocalDate enrollmentDate;
    private String major;
    @Navigate(
            value = RelationType.ManyToMany,
            self = Fields.studentId,
            selfMapping = StudentCourse.Fields.studentId,
            mappingTable = StudentCourse.class,
            targetMapping = StudentCourse.Fields.courseId,
            target = Course.Fields.courseId
    )
    private List<Course> courses;
}
