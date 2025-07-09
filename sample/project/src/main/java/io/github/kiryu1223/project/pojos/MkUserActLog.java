package io.github.kiryu1223.project.pojos;

import java.util.Date;

import io.github.kiryu1223.drink.base.annotation.Column;
import lombok.Data;

@Data
public class MkUserActLog {
    /**
     *
     */
    @Column(primaryKey = true, generatedKey = true)
    private Long id;

    /**
     * 发起用户
     */
    private Long fromUserId;

    /**
     * 目标用户
     */
    private Long toUserId;

    /**
     * 类型 visit like collect
     */
    private String type;

    /**
     * 位置 home
     */
    private String locate;

    /**
     * 内容
     */
    private String content;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;
}