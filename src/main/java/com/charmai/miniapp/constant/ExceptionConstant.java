package com.charmai.miniapp.constant;

import com.charmai.miniapp.entity.WxUserInviteCodeEntity;

/**
 * @Author: Xie
 * @Date: 2023-07-30-15:24
 * @Description:
 */
public class ExceptionConstant {
    /**
     * 用户类错误码 6000X
     */
    public static final Integer USER_NOT_EXIT_EXCEPTION_CODE = 60003;
    public static final String USER_NOT_EXIT_EXCEPTION_MSG = "用户不存在";

    public static final Integer WX_USER_INVITE_CODE_INVALID_EXCEPTION_CODE = 60004;
    public static final String WX_USER_INVITE_CODE_INVALID_EXCEPTION_MSG = "用户邀请码无效";

    /**
     * 积分类错误码
     */
    public static final Integer USER_POINTS_REPEAT_SUBMIT_EXCEPTION_CODE = 80001;
    public static final String USER_POINTS_REPEAT_SUBMIT_EXCEPTION_MSG = "用户积分操作中";

    public static final Integer USER_POINTS_NOT_ENOUGH_EXCEPTION_CODE = 80002;
    public static final String USER_POINTS_NOT_ENOUGH_SUBMIT_EXCEPTION_MSG = "用户积分不足";

    /**
     * 商品类错误码
     */
    public static final Integer PRODUCT_NOT_EXIT_EXCEPTION_CODE = 90002;
    public static final String PRODUCT_NOT_EXIT_EXCEPTION_MSG = "商品不存在";


    public static final Integer USER_PAY_NOTIFY_REPEAT_SUBMIT_EXCEPTION_CODE = 90001;
    public static final String USER_PAY_NOTIFY_REPEAT_SUBMIT_EXCEPTION_MSG = "用户支付回调";

    public static final Integer NOT_SUPPORT_ORDER_TYPE_EXCEPTION_CODE = 90003;
    public static final String NOT_SUPPORT_ORDER_TYPE_EXCEPTION_MSG = "订单模式不支持";

    public static final Integer PHOTO_GENERATE_TASK_REPEAT_SUBMIT_EXCEPTION_CODE = 90004;
    public static final String PHOTO_GENERATE_TASK_REPEAT_SUBMIT_EXCEPTION_MSG = "生成任务列表占用中";


    /**
     * 模板类错误码
     */
    public static final Integer MODEL_TEMPLATE_NOT_EXIT_EXCEPTION_CODE = 100001;
    public static final String MODEL_TEMPLATE_NOT_EXIT_EXCEPTION_MSG = "模板不存在";

    public static final Integer MODEL_TEMPLATE_TYPE_NOT_EXIT_EXCEPTION_CODE = 100002;
    public static final String MODEL_TEMPLATE_TYPE_NOT_EXIT_EXCEPTION_MSG = "模板分类不存在";

    public static final Integer MODEL_TEMPLATE_TYPE_ALREADY_EXIT_EXCEPTION_CODE = 100003;
    public static final String MODEL_TEMPLATE_TYPE_ALREADY_EXIT_EXCEPTION_MSG = "模板分类已存在";
}
