package io.github.kiryu1223.project.pojos;

import io.github.kiryu1223.drink.base.annotation.Table;
import io.github.kiryu1223.drink.base.metaData.IMappingTable;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@FieldNameConstants
@Table("student_courses")
public class StudentCourse implements IMappingTable
{
    private int id;
    private int studentId;
    private int courseId;
    private LocalDateTime selectionDate;
    private BigDecimal score;
}
