package com.charmai.miniapp.adapter.pay;

import com.charmai.miniapp.entity.WxUserPayRecordEntity;

/**
 * @Author: Xie
 * @Date: 2023-08-02-21:56
 * @Description:
 */
public interface Payment {

    public Integer getOrderType();

    public void handelSuccessPayOrder(WxUserPayRecordEntity wxUserPayRecordEntity);

}
