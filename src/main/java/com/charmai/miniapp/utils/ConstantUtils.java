package com.charmai.miniapp.utils;

public class ConstantUtils {
    public static final int ORDER_TYPE_NO_INVITE_CODE = 1;

    public static final int ORDER_TYPE_INVITE_CODE = 2;

    public static final int ORDER_TYPE_POINTS = 3;

    public static final String ORDER_TYPE_INVITE_CODE_BODY = "邀请码支付";

    public static final String ORDER_TYPE_NO_INVITE_CODE_BODY = "无邀请码支付";

    public static final String ORDER_TYPE_POINTS_BODY = "积分充值支付";

    public static final String WX_OUT_TRADE_NO_LOCK_NAME = "WX_OUT_TRADE_NO_LOCK_";

    //无邀请码直接支付额度，单位分
    public static final int NO_INVITE_CODE_PAY_FEE = 390;
//    public static final int NO_INVITE_CODE_PAY_FEE = 2;

    //有邀请码直接支付额度，单位分
    public static final int INVITE_CODE_PAY_FEE = 190;
//    public static final int INVITE_CODE_PAY_FEE = 1;

    //邀请者增加积分额度
    public static final int INVITE_USER_POINTS = 50;

    //被邀请者或直接支付者增加积分额度
    public static final int INVITEE_USER_POINTS = 150;

    //ios充值增加
    public static final int INCREASE_TYPE_RECHARGE = 1;

    //分享增加
    public static final int INCREASE_TYPE_SHARE = 2;

    /**
     * photo 任务状态
     * 0 等待初始开始的任务
     * 1 初始化
     */
    public static final int PHOTO_TASK_STATUS_WAITING_INITIAL_START = 0;
    public static final int PHOTO_TASK_STATUS_INITIAL = 1;

    /**
     * photo 任务类型
     * 0 用户首次生成
     * 1 用户迭代生成
     */
    public static final int PHOTO_TASK_TYPE_FIRST_TIME = 0;
    public static final int PHOTO_TASK_TYPE_ITERATION = 1;


}
