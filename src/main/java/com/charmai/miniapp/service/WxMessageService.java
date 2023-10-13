package com.charmai.miniapp.service;

import cn.binarywang.wx.miniapp.bean.WxMaMessage;

/**
 * @Author: Xie
 * @Date: 2023-08-03-22:26
 * @Description:
 */
public interface WxMessageService {

    public void handleWxMaMessage(WxMaMessage wxMaMessage);
}
