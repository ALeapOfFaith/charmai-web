package com.charmai.miniapp.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.charmai.common.exception.ServiceException;
import com.charmai.miniapp.adapter.pay.Payment;
import com.charmai.miniapp.adapter.pay.PaymentFactory;
import com.charmai.miniapp.constant.ExceptionConstant;
import com.charmai.miniapp.constant.RedisConstant;
import com.charmai.miniapp.entity.*;
import com.charmai.miniapp.service.*;
import com.charmai.miniapp.utils.ConstantUtils;
import com.charmai.miniapp.utils.RedisClientUtils;
import com.charmai.weixin.entity.WxUser;
import com.charmai.weixin.service.WxUserService;
import com.charmai.weixin.utils.ThirdSessionHolder;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("myWxPayService")
public class MyWxPayServiceImpl implements MyWxPayService {

    @Autowired
    WxPayService wxService;

    @Autowired
    WxUserService wxUserService;

    @Autowired
    ProductsService productsService;

    @Autowired
    WxUserPayRecordService wxUserPayRecordService;

    @Autowired
    PaymentFactory paymentFactory;

    @Autowired
    WxUserInviteCodeService wxUserInviteCodeService;

    @Autowired
    WxUserInviteCodeRecordService wxUserInviteCodeRecordService;

    @Override
    public WxPayUnifiedOrderV3Result.JsapiResult mixCreateOrder(String userId) throws WxPayException {
        WxPayUnifiedOrderV3Request wxPayUnifiedOrderRequest = new WxPayUnifiedOrderV3Request();
        String outTradeNo = UUID.randomUUID().toString().replaceAll("-", "");

        WxUser wxUser = wxUserService.getById(userId);
        if (ObjectUtils.isEmpty(wxUser)) {
            throw new ServiceException(ExceptionConstant.USER_NOT_EXIT_EXCEPTION_MSG, ExceptionConstant.USER_NOT_EXIT_EXCEPTION_CODE);
        }
        String openId = wxUser.getOpenId();

//        log.info("user {} create pay order {}, the order type is {}", userId, outTradeNo, mixUnifiedOrderDto.getOrderType());

        //构建用户微信订单支付记录，并存入数据库中
        WxUserPayRecordEntity wxUserPayRecordEntity = new WxUserPayRecordEntity();
        wxUserPayRecordEntity.setWxUserId(userId);
        wxUserPayRecordEntity.setOutTradeNo(outTradeNo);
        wxUserPayRecordEntity.setTradeState("INIT");
        wxUserPayRecordEntity.setCreateTime(new Date());
        wxUserPayRecordEntity.setWxUserId(userId);

        //微信支付结构体构造
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        amount.setCurrency("CNY");

        //判断有没有邀请记录来区分支付模式
        Integer orderType = ConstantUtils.ORDER_TYPE_NO_INVITE_CODE;
        WxUserInviteCodeRecordEntity wxInviteeUserRecord = wxUserInviteCodeRecordService.getOne(new QueryWrapper<WxUserInviteCodeRecordEntity>().eq("wx_invitee_user_id", wxUser.getId()));
        if (!ObjectUtils.isEmpty(wxInviteeUserRecord)) {
            orderType = ConstantUtils.ORDER_TYPE_INVITE_CODE;
            wxUserPayRecordEntity.setInviteCode(wxInviteeUserRecord.getInviteCode());
        }

        //邀请码支付模式
        if (orderType == ConstantUtils.ORDER_TYPE_INVITE_CODE) {
            wxPayUnifiedOrderRequest.setDescription(ConstantUtils.ORDER_TYPE_INVITE_CODE_BODY);
            wxUserPayRecordEntity.setType(ConstantUtils.ORDER_TYPE_INVITE_CODE);
            wxUserPayRecordEntity.setPayFee(BigDecimal.valueOf(ConstantUtils.INVITE_CODE_PAY_FEE));
            amount.setTotal(ConstantUtils.INVITE_CODE_PAY_FEE);

            //非邀请码支付模式
        } else {
            wxPayUnifiedOrderRequest.setDescription(ConstantUtils.ORDER_TYPE_NO_INVITE_CODE_BODY);
            wxUserPayRecordEntity.setType(ConstantUtils.ORDER_TYPE_NO_INVITE_CODE);
            wxUserPayRecordEntity.setPayFee(BigDecimal.valueOf(ConstantUtils.NO_INVITE_CODE_PAY_FEE));
            amount.setTotal(ConstantUtils.NO_INVITE_CODE_PAY_FEE);
        }
//        else if (mixUnifiedOrderDto.getOrderType() == ConstantUtils.ORDER_TYPE_POINTS) {
//            wxPayUnifiedOrderRequest.setDescription(ConstantUtils.ORDER_TYPE_POINTS_BODY);
//            wxUserPayRecordEntity.setType(ConstantUtils.ORDER_TYPE_POINTS);
//
//            //获取积分ID对应信息
//            ProductsEntity productsEntity = productsService.getById(mixUnifiedOrderDto.getProductId());
//            wxUserPayRecordEntity.setPoints(productsEntity.getPoint());
//            wxUserPayRecordEntity.setPayFee(productsEntity.getPrice());
//            amount.setTotal(productsEntity.getPrice().intValue());
//
//
//        }


        //构建微信支付订单对象
        wxPayUnifiedOrderRequest.setNotifyUrl("https://www.charmai.top/weixin/api/pay/notify/order");
        wxPayUnifiedOrderRequest.setPayer(new WxPayUnifiedOrderV3Request.Payer().setOpenid(openId));
        wxPayUnifiedOrderRequest.setOutTradeNo(outTradeNo);
        wxPayUnifiedOrderRequest.setAmount(amount);

        //调用微信支付创建订单
        WxPayUnifiedOrderV3Result.JsapiResult result = wxService.createOrderV3(TradeTypeEnum.JSAPI, wxPayUnifiedOrderRequest);

        //存储微信用户支付订单记录
//        wxUserPayRecordEntity.setPayUrl(result.getH5Url());
        wxUserPayRecordService.save(wxUserPayRecordEntity);

        log.info("user {} create pay order {} success", userId, outTradeNo);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String parseOrderNotifyResult(String notifyData, SignatureHeader header) {
        String outTradeNo;
        String lockName = "";
        try {

            final WxPayNotifyV3Result notifyResult = wxService.parseOrderNotifyV3Result(notifyData, header);
            outTradeNo = notifyResult.getResult().getOutTradeNo();

            log.info("get the weixin call back massage of the order {}", JSON.toJSONString(notifyResult));

            long waitTime = 200;
            lockName = ConstantUtils.WX_OUT_TRADE_NO_LOCK_NAME + outTradeNo;
            if (!RedisClientUtils.tryLock(lockName, waitTime, TimeUnit.MILLISECONDS)) {
                RedisClientUtils.unLock(lockName);
                log.error("parseOrderNotifyResult repeat submit exception,outTradeNo: {} ", outTradeNo);
                throw new ServiceException(ExceptionConstant.USER_PAY_NOTIFY_REPEAT_SUBMIT_EXCEPTION_MSG,
                        ExceptionConstant.USER_PAY_NOTIFY_REPEAT_SUBMIT_EXCEPTION_CODE);
            }

            //根据订单号获取数据库对应订单记录
            QueryWrapper<WxUserPayRecordEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WxUserPayRecordEntity::getOutTradeNo, outTradeNo);
            WxUserPayRecordEntity wxUserPayRecordEntity = wxUserPayRecordService.getOne(queryWrapper);

            String tradeState = notifyResult.getResult().getTradeState();

            if (wxUserPayRecordEntity != null && !tradeState.equals(wxUserPayRecordEntity.getTradeState())) {

                //订单支付成功时更新对应邀请码或积分记录
                if (notifyResult.getResult().getTradeState().equals("SUCCESS")) {
                    //根据不同的模式生成不同的业务处理类
                    Payment payment = paymentFactory.productPayment(wxUserPayRecordEntity.getType());
                    payment.handelSuccessPayOrder(wxUserPayRecordEntity);
                }

                wxUserPayRecordEntity.setTradeState(tradeState);
                wxUserPayRecordEntity.setUpdateTime(new Date());
                wxUserPayRecordService.saveOrUpdate(wxUserPayRecordEntity);
            }
        } catch (Exception e) {
            return WxPayNotifyResponse.fail("失败");
        } finally {
            String finalLockName = lockName;
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    log.info("事务提交成功,lockName: {} ,status:{} ", finalLockName, status);
                    RedisClientUtils.unLock(finalLockName);
                }
            });
        }
        return WxPayNotifyResponse.success("成功");
    }


}
