package com.charmai.miniapp.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaMsgService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.charmai.miniapp.entity.WxUserPayRecordEntity;
import com.charmai.miniapp.mapper.WxUserPayRecordMapper;
import com.charmai.miniapp.service.MyWxPayService;
import com.charmai.miniapp.service.WxMessageService;
import com.charmai.miniapp.service.WxUserPayRecordService;
import com.charmai.weixin.config.WxMaConfiguration;
import com.charmai.weixin.entity.WxUser;
import com.charmai.weixin.service.WxUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @Author: Xie
 * @Date: 2023-08-03-22:26
 * @Description:
 */
@Service
@Slf4j
public class WxMessageServiceImpl implements WxMessageService {


    @Autowired
    WxUserPayRecordService wxUserPayRecordService;

    @Autowired
    WxUserPayRecordMapper wxUserPayRecordMapper;

    @Autowired
    WxUserService wxUserService;


    public void handleWxMaMessage(WxMaMessage wxMaMessage) {

        String openId = wxMaMessage.getOpenId();
        WxUser wxUser = wxUserService.getOne(new QueryWrapper<WxUser>().eq("open_id", openId));

        if (!ObjectUtils.isEmpty(wxUser)) {
            QueryWrapper<WxUserPayRecordEntity> queryWrapper = Wrappers.query();
            queryWrapper.orderByDesc("create_time");
            queryWrapper.last("LIMIT 1");
            WxUserPayRecordEntity wxUserPayRecordEntity = wxUserPayRecordMapper.selectOne(queryWrapper);
            if (!ObjectUtils.isEmpty(wxUserPayRecordEntity)) {
                sendWxMessage(wxUserPayRecordEntity);
            }

        }

    }

    public void sendWxMessage(WxUserPayRecordEntity wxUserPayRecordEntity) {
        WxMaService wxMaService = WxMaConfiguration.getMaService("wx3ac892db62e7e7c9");
        try {
            WxMaKefuMessage wxMaKefuMessage = new WxMaKefuMessage();
            wxMaKefuMessage.setToUser(wxUserPayRecordEntity.getWxUserId());
            wxMaKefuMessage.setMsgType("miniprogrampage");
            WxMaKefuMessage.KfMaPage kfMaPage = new WxMaKefuMessage.KfMaPage();
            kfMaPage.setTitle("点我去支付");
            kfMaPage.setPagePath(wxUserPayRecordEntity.getPayUrl());
            wxMaKefuMessage.setMaPage(kfMaPage);
            WxMaMsgService wxMaMsgService = wxMaService.getMsgService();
            wxMaMsgService.sendKefuMsg(wxMaKefuMessage);
            log.info("start to sendKefuMsg");
        } catch (Exception e) {
            log.error("sendWxMessage error:{}", e.toString());
        }

    }

}
