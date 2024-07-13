package io.github.kiryu1223.drink.pojos;

import io.github.kiryu1223.drink.annotation.Column;
import io.github.kiryu1223.drink.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Table("t_topic")
@Data
public class Topic
{
    @Column
    private String id;
    @Column("stars")
    private Integer stars;
    private String title;
    private LocalDateTime createTime;
    private String alias;
}
