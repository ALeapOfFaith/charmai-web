package com.charmai.miniapp.service;

import com.charmai.miniapp.entity.MixUnifiedOrderDto;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;

public interface MyWxPayService {

    WxPayUnifiedOrderV3Result.JsapiResult mixCreateOrder(String userId) throws WxPayException;

    String parseOrderNotifyResult(String notifyData, SignatureHeader header);



}
