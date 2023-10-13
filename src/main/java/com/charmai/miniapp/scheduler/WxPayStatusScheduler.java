package com.charmai.miniapp.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.charmai.common.exception.ServiceException;
import com.charmai.miniapp.adapter.pay.Payment;
import com.charmai.miniapp.adapter.pay.PaymentFactory;
import com.charmai.miniapp.constant.ExceptionConstant;
import com.charmai.miniapp.constant.RedisConstant;
import com.charmai.miniapp.entity.WxUserPayRecordEntity;
import com.charmai.miniapp.service.WxUserPayRecordService;
import com.charmai.miniapp.utils.ConstantUtils;
import com.charmai.miniapp.utils.RedisClientUtils;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Xie
 * @Date: 2023-08-01-23:23
 * @Description:
 */
@Slf4j
@Component
public class WxPayStatusScheduler {

    @Autowired
    WxPayService wxService;

    @Autowired
    WxUserPayRecordService wxUserPayRecordService;

    @Autowired
    PaymentFactory paymentFactory;


    @Scheduled(initialDelay = 10000, fixedRate = 5000) //第一次延迟10秒后执行，之后按fixedRate的规则每5秒执行一次
    public void execute() {

        log.info("start to execute wxPayStatusScheduler");
        /**
         * 查询数据库对应未完成记录的订单
         * 1.计算过去两小时的订单（成功，初始化，未支付）
         */
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHoursAgo = now.minusHours(1);
        List<String> tradeStates = new ArrayList<>();
        tradeStates.add("USERPAYING");
        tradeStates.add("INIT");
        tradeStates.add("NOTPAY");
        List<WxUserPayRecordEntity> wxUserPayRecordEntitys = wxUserPayRecordService.list(new QueryWrapper<WxUserPayRecordEntity>()
                .in("trade_state", tradeStates)
                .ge("create_time", oneHoursAgo)
                .le("create_time", now));
        for (WxUserPayRecordEntity wxUserPayRecordEntity : wxUserPayRecordEntitys) {
            handleWxPayStatus(wxUserPayRecordEntity);
        }

    }

    //    @Transactional(rollbackFor = Exception.class)
    public void handleWxPayStatus(WxUserPayRecordEntity wxUserPayRecordEntity) {
        String outTradeNo = wxUserPayRecordEntity.getOutTradeNo();
        log.info("start to handleWxPayStatus outTradeNo:{}", outTradeNo);
        long waitTime = 200;
        String lockName = ConstantUtils.WX_OUT_TRADE_NO_LOCK_NAME + outTradeNo;
        if (!RedisClientUtils.tryLock(lockName, waitTime, TimeUnit.MILLISECONDS)) {
            RedisClientUtils.unLock(lockName);
            log.error("tryLock fail lockName:{}", lockName);
        }

        try {

            WxPayOrderQueryV3Result wxPayOrderQueryV3Result = wxService.queryOrderV3(null, outTradeNo);
            String tradeState = wxPayOrderQueryV3Result.getTradeState();
            if ("SUCCESS".equals(tradeState)) {
                /**
                 * 根据不同的模式生成不同的业务处理类
                 */
                Payment payment = paymentFactory.productPayment(wxUserPayRecordEntity.getType());
                payment.handelSuccessPayOrder(wxUserPayRecordEntity);
            }
            wxUserPayRecordEntity.setTradeState(tradeState);
            wxUserPayRecordEntity.setUpdateTime(new Date());
            wxUserPayRecordService.saveOrUpdate(wxUserPayRecordEntity);

        } catch (WxPayException e) {
            log.error("queryWxPay exception outTradeNo:{}", outTradeNo);
            throw new RuntimeException(e);
        } finally {
//            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//                @Override
//                public void afterCompletion(int status) {
//                    log.info("事务提交成功,lockName: {} ,status:{} ", lockName, status);
//
//                }
//            });
            RedisClientUtils.unLock(lockName);
        }


    }


}
