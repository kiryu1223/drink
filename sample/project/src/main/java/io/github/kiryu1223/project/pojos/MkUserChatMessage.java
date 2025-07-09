package io.github.kiryu1223.project.pojos;

import java.util.Date;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.Navigate;
import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.core.api.ITable;
import io.github.kiryu1223.expressionTree.expressions.annos.Getter;
import io.github.kiryu1223.expressionTree.expressions.annos.Setter;
import lombok.Data;

@Data
@Getter
@Setter
public class MkUserChatMessage {
    /**
     *
     */
    @Column(primaryKey = true, generatedKey = true)
    private Long id;

    /**
     * 来源用户
     */
    private Long fromUserId;
    @Navigate(value = RelationType.ManyToOne,
            self = "fromUserId",
            target = "id")
    private MkUser fromUser;


    /**
     * 目标用户
     */
    private Long toUserId;

    @Navigate(value = RelationType.ManyToOne,
            self = "toUserId",
            target = "id")
    private MkUser toUser;

    /**
     * 消息类型 text pic audio video
     */
    private String type;

    /**
     * 消息内容
     */
    private String content;

    /**
     *
     */
    private Date createTime;

    /**
     * 已读状态 0否 1是
     */
    private Integer readFlag;

    /**
     * 已读时间
     */
    private Date readTime;
}