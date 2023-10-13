package com.charmai.miniapp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.charmai.miniapp.entity.WxUserPointsEntity;
import com.charmai.miniapp.service.impl.PageUtils;
import com.charmai.miniapp.vo.IncreasePointsRequestVo;
import com.charmai.miniapp.vo.WxUserPointsResponseVo;

import java.util.Map;

/**
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-07-30 17:55:47
 */
public interface WxUserPointsService extends IService<WxUserPointsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取用户的总积分
     *
     * @param userId
     * @return
     */
    WxUserPointsResponseVo getWxUserTotalPoints(String userId);

    /**
     * 增加用户积分
     *
     * @param userId     用户id
     * @param points     积分
     * @param outTradeNo 商品订单号
     */
    void increaseWxUserPoints(String userId, long points, String outTradeNo, Integer increaseType);

    /**
     * 扣除用户积分
     *
     * @param userId
     * @param points
     */
    void deductWxUserPoints(String userId, long points);

    void operateIncreasePoints(IncreasePointsRequestVo increasePointsRequest);


}

