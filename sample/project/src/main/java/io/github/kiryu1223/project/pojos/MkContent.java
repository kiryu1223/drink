package io.github.kiryu1223.project.pojos;

import java.util.Date;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.IgnoreColumn;
import lombok.Data;

@Data
public class MkContent {
    /**
     *
     */
    @Column(primaryKey = true, generatedKey = true)
    private Long id;

    /**
     * 发布用户
     */
    private Long userId;


    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 1通告 2约单 3作品
     */
    private Integer type;

    /**
     * 标题
     */
    private String title;

    /**
     * 推荐 0否 1是
     */
    private Integer recommend;

    /**
     * 图片
     */
    private String pics;

    /**
     * 通告分类
     */
    private String classify;

    /**
     * 身份
     */
    private String identity;

    /**
     * 面向地区
     */
    private String area;

    /**
     * 截止时间
     */
    private Date lastTime;

    /**
     * 性别
     */
    private String gender;

    /**
     * 费用类型
     */
    private String feeType;

    /**
     * 费用（区间）
     */
    private String feeRange;

    /**
     * 费用单位
     */
    private String feeUnit;

    /**
     * 所需人数
     */
    private Integer requiredNum;

    /**
     * 描述
     */
    private String descr;

    /**
     * 时间
     */
    private Date time;

    /**
     * 合作地点
     */
    private String addr;

    /**
     * 用户gps定位
     */
    private String gpsAddr;

    /**
     * 约单-接单通告/作品-作品标签
     */
    private String tags;

    /**
     * 1待审核 2已驳回 3通过 4结束
     */
    private Integer status;

    /**
     * 浏览数
     */
    private Integer visit;

    /**
     * 点赞数
     */
    private Integer likes;


    @IgnoreColumn
    private Integer likeState = 0;
}