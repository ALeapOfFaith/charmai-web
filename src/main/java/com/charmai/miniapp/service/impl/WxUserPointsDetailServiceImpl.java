package com.charmai.miniapp.service.impl;

import com.charmai.miniapp.entity.WxUserPointsDetailEntity;
import com.charmai.miniapp.mapper.WxUserPointsDetailMapper;
import com.charmai.miniapp.service.WxUserPointsDetailService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("wxUserPointsDetailService")
public class WxUserPointsDetailServiceImpl extends ServiceImpl<WxUserPointsDetailMapper, WxUserPointsDetailEntity> implements WxUserPointsDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WxUserPointsDetailEntity> page = this.page(
                new Query<WxUserPointsDetailEntity>().getPage(params),
                new QueryWrapper<WxUserPointsDetailEntity>()
        );

        return new PageUtils(page);
    }

}