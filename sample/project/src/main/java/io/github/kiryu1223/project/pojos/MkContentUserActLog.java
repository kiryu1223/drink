package io.github.kiryu1223.project.pojos;

import java.util.Date;

import io.github.kiryu1223.drink.base.annotation.Column;
import lombok.Data;

@Data
public class MkContentUserActLog {
    /**
     *
     */
    @Column(generatedKey = true)
    private Long id;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 内容id
     */
    private Long contentId;

    /**
     * 类型 like visit
     */
    private String type;

    /**
     * 时间
     */
    private Date createTime;
}