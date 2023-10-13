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
public class PointsPayment extends PaymentAdapter {

    @Autowired
    WxUserPointsService wxUserPointsService;

    @Override
    public Integer getOrderType() {
        return ConstantUtils.ORDER_TYPE_POINTS;
    }

    /**
     * 积分支付，需要保存积分支付记录，并更新对应用户的积分情况到数据库中
     *
     * @param wxUserPayRecordEntity
     */
    @Override
    public void handelSuccessPayOrder(WxUserPayRecordEntity wxUserPayRecordEntity) {

        wxUserPointsService.increaseWxUserPoints(wxUserPayRecordEntity.getWxUserId(), wxUserPayRecordEntity.getPoints(), wxUserPayRecordEntity.getOutTradeNo(),null);

    }
}
