package com.charmai.miniapp.api;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.binarywang.wx.miniapp.message.WxMaXmlOutMessage;
import com.charmai.common.core.domain.R;
import com.charmai.miniapp.service.WxMessageService;
import com.charmai.weixin.config.WxMaConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @Author: Xie
 * @Date: 2023-08-03-23:16
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/weixin/api/message")
public class WxMaMsgApi {

//    @Autowired
//    private WxMaService wxMaService;

    @Autowired
    WxMessageService wxMessageService;

    /**
     * 消息校验，确定是微信发送的消息
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     * @throws Exception
     */
    @GetMapping
    public String doGet(String signature, String timestamp, String nonce, String echostr) {
        WxMaService wxMaService = WxMaConfiguration.getMaService("wx3ac892db62e7e7c9");
        // 消息合法
        if (wxMaService.checkSignature(timestamp, nonce, signature)) {
            log.info("-------------微信小程序消息验证通过");
            return echostr;
        }
        // 消息签名不正确，说明不是公众平台发过来的消息
        return null;
    }

    /**
     * 微信小程序事件推送
     *
     * @return
     * @throws Exception
     */
    @PostMapping
    public R doPost(@RequestBody String requestBody,
                    @RequestParam(name = "msg_signature", required = false) String msgSignature,
                    @RequestParam(name = "encrypt_type", required = false) String encryptType,
                    @RequestParam(name = "signature", required = false) String signature,
                    @RequestParam("timestamp") String timestamp,
                    @RequestParam("nonce") String nonce) throws IOException {
        // 解密消息
        WxMaMessage wxMaMessage = getWxMaMessage(requestBody, msgSignature, encryptType, signature, timestamp, nonce);
        log.info("收到消息：{}", wxMaMessage);
//        wxMessageService.handleWxMaMessage(wxMaMessage);
//        // 转到客服
//        WxMaXmlOutMessage wxMaXmlOutMessage = new WxMaXmlOutMessage();
//        wxMaXmlOutMessage.setToUserName(wxMaMessage.getFromUser());
//        wxMaXmlOutMessage.setFromUserName(wxMaMessage.getToUser());
//        // 转发到客服
//        wxMaXmlOutMessage.setMsgType("transfer_customer_service");
//        wxMaXmlOutMessage.setCreateTime(System.currentTimeMillis());
//        return toWxMaOutMessage(wxMaXmlOutMessage, encryptType);
        return R.ok();
    }

    /**
     * 解析消息
     *
     * @param requestBody
     * @param msgSignature
     * @param encryptType
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public WxMaMessage getWxMaMessage(String requestBody, String msgSignature, String encryptType, String signature, String timestamp, String nonce) {
        WxMaService wxMaService = WxMaConfiguration.getMaService("wx3ac892db62e7e7c9");
        // 明文传输
        if (StringUtils.isBlank(encryptType)) {
            // json
            if (WxMaConstants.MsgDataFormat.JSON.equals(wxMaService.getWxMaConfig().getMsgDataFormat())) {
                return WxMaMessage.fromJson(requestBody);
            } else {
                return WxMaMessage.fromXml(requestBody);
            }
        }
        // aes加密
        else if ("aes".equals(encryptType)) {
            // json
            if (WxMaConstants.MsgDataFormat.JSON.equals(wxMaService.getWxMaConfig().getMsgDataFormat())) {
                return WxMaMessage.fromEncryptedJson(requestBody, wxMaService.getWxMaConfig());
            }
            // xml
            else {
                return WxMaMessage.fromEncryptedXml(requestBody, wxMaService.getWxMaConfig(), timestamp, nonce, msgSignature);
            }
        }
        throw new RuntimeException("不可识别的消息加密方式");
    }

    /**
     * 返回给微信服务器的消息
     *
     * @param wxMaXmlOutMessage
     * @param encryptType
     * @return
     */
    public String toWxMaOutMessage(WxMaXmlOutMessage wxMaXmlOutMessage, String encryptType) {
        WxMaService wxMaService = WxMaConfiguration.getMaService("wx3ac892db62e7e7c9");
        // 明文传输
        if (StringUtils.isBlank(encryptType)) {
            return wxMaXmlOutMessage.toXml();
        }
        // aes加密
        else if ("aes".equals(encryptType)) {
            return wxMaXmlOutMessage.toEncryptedXml(wxMaService.getWxMaConfig());
        }
        throw new RuntimeException("不可识别的消息加密方式");
    }


}
