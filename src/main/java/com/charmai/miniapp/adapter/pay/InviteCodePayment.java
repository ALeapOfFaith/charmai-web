package com.charmai.miniapp.adapter.pay;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.charmai.miniapp.entity.WxUserInviteCodeEntity;
import com.charmai.miniapp.entity.WxUserPayRecordEntity;
import com.charmai.miniapp.service.WxUserInviteCodeRecordService;
import com.charmai.miniapp.service.WxUserInviteCodeService;
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
public class InviteCodePayment extends PaymentAdapter {

    @Autowired
    WxUserInviteCodeService wxUserInviteCodeService;

    @Autowired
    WxUserPointsService wxUserPointsService;

    @Autowired
    WxUserInviteCodeRecordService wxUserInviteCodeRecordService;

    @Override
    public Integer getOrderType() {
        return ConstantUtils.ORDER_TYPE_INVITE_CODE;
    }

    /**
     * 邀请码支付，需要保存邀请码支付记录到数据库中
     *
     * @param wxUserPayRecordEntity
     */
    @Override
    public void handelSuccessPayOrder(WxUserPayRecordEntity wxUserPayRecordEntity) {

        String outTradeNo = wxUserPayRecordEntity.getOutTradeNo();

        //根据邀请码获取分享该邀请者的用户ID
        QueryWrapper<WxUserInviteCodeEntity> wxUserInviteCodeQueryWrapper = new QueryWrapper<>();
        wxUserInviteCodeQueryWrapper.lambda().eq(WxUserInviteCodeEntity::getInviteCode, wxUserPayRecordEntity.getInviteCode());
        String wxUserId = wxUserInviteCodeService.getOne(wxUserInviteCodeQueryWrapper).getWxUserId();

        //增加邀请码分享者50积分
        wxUserPointsService.increaseWxUserPoints(wxUserId, ConstantUtils.INVITE_USER_POINTS, outTradeNo, null);

        //被邀请者支付完成后，增加被邀请者150积分
        wxUserPointsService.increaseWxUserPoints(wxUserPayRecordEntity.getWxUserId(), ConstantUtils.INVITEE_USER_POINTS, outTradeNo, null);

    }

}
