package com.charmai.miniapp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-07-30 17:55:48
 */
@Data
@TableName("wx_user_points_detail")
public class WxUserPointsDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     *
     */
    private String wxUserId;
    /**
     * 积分类型（0：增加；1：扣除）
     */
    private Integer type;
    /**
     * 积分详情
     */
    private Long point;
    /**
     *
     */
    private Date createTime;
    /**
     * 逻辑删除标记（0：显示；1：隐藏）
     */
    private String delFlag;
    /**
     * 支付订单号
     */
    private String outTradeNo;

    /**
     * 增加积分类型（0：充值支付；1.分享）
     */
    private Integer increaseType;

}
