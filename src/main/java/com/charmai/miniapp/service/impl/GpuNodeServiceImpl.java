package com.charmai.miniapp.service.impl;

import com.charmai.miniapp.mapper.GpuNodeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.charmai.miniapp.entity.GpuNodeEntity;
import com.charmai.miniapp.service.GpuNodeService;


@Service("gpuNodeService")
public class GpuNodeServiceImpl extends ServiceImpl<GpuNodeMapper, GpuNodeEntity> implements GpuNodeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        IPage<GpuNodeEntity> page = this.page(
                new Query<GpuNodeEntity>().getPage(params),
                new QueryWrapper<GpuNodeEntity>()
        );
        return new PageUtils(page);
    }

    public List<GpuNodeEntity> queryAllAvailable() {
        return baseMapper.selectList(new QueryWrapper<GpuNodeEntity>().eq("status", 0));
    }
    public GpuNodeEntity getNodeByAddress(String address) {
        return baseMapper.getNodeByAddress(address);
    }

    public void updateEntity(String address) {
        GpuNodeEntity nodeByAddress = baseMapper.getNodeByAddress(address);

    }

}