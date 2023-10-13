package com.charmai.miniapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charmai.miniapp.entity.WxUserPointsDetailEntity;
import com.charmai.miniapp.service.impl.PageUtils;


import java.util.Map;

/**
 * 
 *
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-07-30 17:55:48
 */
public interface WxUserPointsDetailService extends IService<WxUserPointsDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

