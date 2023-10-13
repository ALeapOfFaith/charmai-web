package com.charmai.miniapp.service.impl;

import com.charmai.miniapp.entity.LoraEntity;
import com.charmai.miniapp.mapper.LoraInfoMapper;
import com.charmai.miniapp.service.LoraService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("loraService")
public class LoraServiceImpl implements LoraService {
    final
    LoraInfoMapper loraInfoMapper;

    public LoraServiceImpl(LoraInfoMapper loraInfoMapper) {
        this.loraInfoMapper = loraInfoMapper;
    }

    @Override
    public List<LoraEntity> getLoraInfo(String template_id){
        return loraInfoMapper.getLoraInfo(template_id);
    }
}
