package com.charmai.miniapp.service.impl;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.charmai.common.core.domain.R;
import com.charmai.miniapp.service.MessageService;
import com.charmai.weixin.config.WxMaConfiguration;
import com.charmai.weixin.utils.ThirdSessionHolder;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author
 * @since
 */
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public R sendMessage(String openId) {
        WxMaService wxMaService = WxMaConfiguration.getMaService("wx3ac892db62e7e7c9");
        String userId = ThirdSessionHolder.getThirdSession().getOpenId();
        // 测试
        List<WxMaSubscribeMessage.MsgData> msgData = Arrays.asList(
                new WxMaSubscribeMessage.MsgData("thing1", "寻寻觅觅，终于找到了平行时空的你"),
                new WxMaSubscribeMessage.MsgData("thing14", "哇哦！这么靓的你！快去跟你的朋友们分享吧")
        );
        try {
            WxMaSubscribeMessage message = WxMaSubscribeMessage.builder()
                    // 要给谁发送
                    .toUser(openId)
                    // 模板id
                    .templateId("HpjLwwYJapMeifqu7RAo-tZVUtrve_kJ8-ICzLqN9vw")
                    .page("pages/index/index")
                    .miniprogramState("formal")
                    // 数据
                    .data(msgData)
                    .build();
            wxMaService.getMsgService().sendSubscribeMsg(message);
            return R.ok("发送成功");
        } catch (WxErrorException e) {
            log.error(e.toString());
            return R.fail(e.getError().getErrorMsg());
        }
    }
    @Override
    public R sendGeneratePhotoMessage(String openId) {
        WxMaService wxMaService = WxMaConfiguration.getMaService("wx3ac892db62e7e7c9");
//        String userId = ThirdSessionHolder.getThirdSession().getOpenId();
        // 测试
        List<WxMaSubscribeMessage.MsgData> msgData = Arrays.asList(
                new WxMaSubscribeMessage.MsgData("thing1", "无限场景,花样姿势,记录最美的你!"),
                new WxMaSubscribeMessage.MsgData("thing14", "哇哦!这么靓的你!快去跟你的朋友们分享吧")
        );
        try {
            WxMaSubscribeMessage message = WxMaSubscribeMessage.builder()
                    // 要给谁发送
                    .toUser(openId)
                    // 模板id
                    .templateId("HpjLwwYJapMeifqu7RAo-tZVUtrve_kJ8-ICzLqN9vw")
                    .page("pages/index/index")
                    .miniprogramState("formal")
                    // 数据
                    .data(msgData)
                    .build();
            wxMaService.getMsgService().sendSubscribeMsg(message);
            return R.ok("发送成功");
        } catch (WxErrorException e) {
            log.error(e.toString());
            return R.fail(e.getError().getErrorMsg());
        }
    }
}


