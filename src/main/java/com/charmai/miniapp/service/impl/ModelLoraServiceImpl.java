package com.charmai.miniapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charmai.miniapp.entity.ModelLoraEntity;
import com.charmai.miniapp.mapper.ModelLoraMapper;
import com.charmai.miniapp.service.ModelLoraService;
import org.springframework.stereotype.Service;
import java.util.Map;



@Service("modelLoraService")
public class ModelLoraServiceImpl extends ServiceImpl<ModelLoraMapper, ModelLoraEntity> implements ModelLoraService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ModelLoraEntity> page = this.page(
                new Query<ModelLoraEntity>().getPage(params),
                new QueryWrapper<ModelLoraEntity>()
        );

        return new PageUtils(page);
    }

}