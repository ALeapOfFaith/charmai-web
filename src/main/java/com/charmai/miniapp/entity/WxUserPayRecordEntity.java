package com.charmai.miniapp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("wx_user_pay_record")
public class WxUserPayRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId
    private Long id;

    /**支付用户ID*/
    private String wxUserId;

    /**支付订单号*/
    private String outTradeNo;

    /**支付类型（1：无邀请码支付；2：邀请码支付；3：积分支付）*/
    private Integer type;

    /**支付金额，以分为单位*/
    private BigDecimal payFee;

    /**支付链接*/
    private String payUrl;


    /**
     *     1   交易状态，枚举值：
     *    SUCCESS：支付成功
     *    REFUND：转入退款
     *    NOTPAY：未支付
     *    CLOSED：已关闭
     *    REVOKED：已撤销（付款码支付）
     *    USERPAYING：用户支付中（付款码支付）
     *    PAYERROR：支付失败(其他原因，如银行返回失败)
     */
    private  String tradeState;

    /**
     * 邀请码支付模式
     * 邀请码
     */
    private String inviteCode;

//    /**
//     *邀请码支付模式
//     * 邀请用户ID
//     */
//    private String wxInviteeUserId;

    /**积分数*/
    private long points;

    /**创建时间*/
    private Date createTime;

    /**更新时间*/
    private Date updateTime;
}
