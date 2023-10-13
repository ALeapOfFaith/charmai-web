package com.charmai.miniapp.adapter.pay;

import com.charmai.miniapp.entity.WxUserPayRecordEntity;
import com.charmai.miniapp.service.WxUserPointsService;
import com.charmai.miniapp.utils.ConstantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Xie
 * @Date: 2023-08-02-22:00
 * @Description:
 */
@Component
public class NoInviteCodePayment extends PaymentAdapter {

    @Autowired
    WxUserPointsService wxUserPointsService;


    @Override
    public Integer getOrderType() {
        return ConstantUtils.ORDER_TYPE_NO_INVITE_CODE;
    }

    @Override
    public void handelSuccessPayOrder(WxUserPayRecordEntity wxUserPayRecordEntity) {
        //无邀请码支付，用户增加150积分
        wxUserPointsService.increaseWxUserPoints(wxUserPayRecordEntity.getWxUserId(), ConstantUtils.INVITEE_USER_POINTS, wxUserPayRecordEntity.getOutTradeNo(), null);
    }
}
