package io.github.kiryu1223.project.pojos;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.annotation.*;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.core.api.ITable;
import io.github.kiryu1223.drink.core.util.List;
import io.github.kiryu1223.expressionTree.expressions.annos.Getter;
import io.github.kiryu1223.expressionTree.expressions.annos.Setter;
import io.github.kiryu1223.expressionTree.util.ReflectUtil;
import io.github.kiryu1223.project.handler.GenderHandler;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;


@Data
@Table("employees")
@FieldNameConstants
public class Employee implements ITable
{
    @Column("emp_no")
    private long number;
    @Column("birth_date")
    private LocalDate birthDay;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    private Gender gender;
    @Column("hire_date")
    private LocalDate hireDay;
    @Navigate(
            value = RelationType.OneToMany,
            self = Fields.number,
            target = Salary.Fields.empNumber
    )
    private List<Salary> salaries;



    public String toString(IConfig config)
    {
        java.util.List<String> strings = new ArrayList<>();
        for (FieldMetaData field : config.getMetaData(getClass()).getFields())
        {
            String fieldName = field.getFieldName();
            if (fieldName.startsWith("val$")) continue;
            strings.add(fieldName + "=" + field.getValueByObject(this));
        }
        return "(" + String.join(",", strings) + ")";
    }
}
