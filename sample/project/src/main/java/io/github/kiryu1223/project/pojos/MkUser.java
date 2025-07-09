package io.github.kiryu1223.project.pojos;

import java.math.BigDecimal;
import java.util.Date;

import io.github.kiryu1223.drink.base.annotation.Column;
import io.github.kiryu1223.drink.base.annotation.IgnoreColumn;
import io.github.kiryu1223.drink.core.api.ITable;
import lombok.Data;

@Data
public class MkUser implements ITable {
    /**
     *
     */
    @Column(primaryKey = true, generatedKey = true)
    private Long id;

    /**
     * 邀请人
     */
    private Long inviteId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码 brypt
     */
    private String password;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 激活
     */
    private Integer activation;

    /**
     * 会员组id
     */
    private Long gid;

    /**
     * 微信小程序openid
     */
    private String wxminiOpenid;

    /**
     * 积分
     */
    @Column(generatedKey = true)
    private Integer score;

    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 登录次数
     */
    private Integer loginCount;

    /**
     * 注册ip
     */
    private String registerIp;

    /**
     * 最后登录ip
     */
    private String lastLoginIp;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 身份
     */
    private String identity;

    /**
     * 地区
     */
    private String area;

    /**
     * 金币
     */
    @Column(generatedKey = true)
    private Integer coin;

    /**
     * 联系人
     */
    private String lxr;

    /**
     * 微信号
     */
    private String wxNo;

    /**
     * qq号
     */
    private String qqNo;

    /**
     * 抖音快手号
     */
    private String dyksNo;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 微信名片二维码
     */
    private String wxQrcode;

    /**
     * 身高
     */
    private String height;

    /**
     * 体重
     */
    private String weight;

    /**
     * 三围
     */
    private String sanwei;

    /**
     * 鞋码
     */
    private String size;

    /**
     * 自我介绍
     */
    private String descr;

    /**
     * 形象视频
     */
    private String videos;

    /**
     * 形象照片
     */
    private String photos;

    /**
     * 相关经历
     */
    private String live;

    /**
     * 相关经历照片
     */
    private String livePics;

    /**
     * 身份标签
     */
    private String identityTags;

    /**
     * 是否实名认证
     */
    @Column(generatedKey = true)
    private Integer isIdcardAuth;

    /**
     * 是否身份认证
     */
    @Column(generatedKey = true)
    private Integer isIdentityAuth;

    /**
     * 是否企业认证
     */
    @Column(generatedKey = true)
    private Integer isOrgAuth;

    /**
     * 创建时间
     */
    @Column(generatedKey = true)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(generatedKey = true)
    private Date updateTime;

    /**
     * 接单通告标签
     */
    private String noticeTags;

    /**
     * 形象标签
     */
    private String styleTags;

    /**
     * 主页背景
     */
    private String homeBg;

    /**
     * 人气（被浏览）
     */
    @Column(generatedKey = true)
    private Integer popularity;

    /**
     * 浏览（浏览了别人）
     */
    @Column(generatedKey = true)
    private Integer viewed;

    /**
     * 关注了别人
     */
    @Column(generatedKey = true)
    private Integer attention;

    /**
     * 粉丝（被关注）
     */
    @Column(generatedKey = true)
    private Integer fans;

    /**
     * 动态数量
     */
    @Column(generatedKey = true)
    private Integer posts;

    /**
     * 收到的赞
     */
    @Column(generatedKey = true)
    private Integer likes;

    /**
     * 是否会员
     */
    @Column(generatedKey = true)
    private Integer vip;

    /**
     * 会员开始时间
     */
    private Date vipStartTime;

    /**
     * 会员结束时间
     */
    private Date vipEndTime;

    /**
     * 是否已交信用担保
     */
    @Column(generatedKey = true)
    private Integer isSurety;

    /**
     * 保证金金额
     */
    @Column(generatedKey = true)
    private BigDecimal suretyAmount;

    /**
     * 是否艺人
     */
    @Column(generatedKey = true)
    private Integer isYiren;

    /**
     * 是否注销
     */
    @Column(generatedKey = true)
    private Integer isDel;

    /**
     * 微信安全trace_id
     */
    private String headpicTraceId;

    @Column(generatedKey = true)
    private Integer wechatVisible;

    @Column(generatedKey = true)
    private Integer mobileVisible;

    /**
     * 最后活跃时间
     */
    private Date lastActTime;
    @IgnoreColumn
    private Integer isOnLine = 0;

    public Integer getIsOnLine() {
//        if (lastActTime == null) {
//            return 0;
//        }
//        //Date deadTime = DateUtil.date().offset(DateField.MINUTE, -5);
//        if (deadTime.after(lastActTime)) {
//            return 0;
//        }
        return 1;
    }

    @IgnoreColumn
    private Integer follow = 0;

    @IgnoreColumn
    private Integer likeState = 0;
}