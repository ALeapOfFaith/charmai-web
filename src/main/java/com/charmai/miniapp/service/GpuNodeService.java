package com.charmai.miniapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charmai.miniapp.entity.GpuNodeEntity;
import com.charmai.miniapp.service.impl.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-07-26 18:19:34
 */
public interface GpuNodeService extends IService<GpuNodeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<GpuNodeEntity> queryAllAvailable();

    GpuNodeEntity getNodeByAddress(String address);
}

