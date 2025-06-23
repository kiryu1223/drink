package io.github.kiryu1223.project.pojos;

import io.github.kiryu1223.drink.base.annotation.Table;
import io.github.kiryu1223.drink.base.metaData.IMappingTable;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldNameConstants
@Table("stu_cou")
public class StudentCourse implements IMappingTable
{
    private Integer stuId;
    private Integer couId;

    private List<Students> students;
}
