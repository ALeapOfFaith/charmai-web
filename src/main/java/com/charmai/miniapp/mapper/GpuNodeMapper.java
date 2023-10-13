package com.charmai.miniapp.mapper;

import com.charmai.miniapp.entity.GpuNodeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-07-26 18:19:34
 */
@Mapper
public interface GpuNodeMapper extends BaseMapper<GpuNodeEntity> {

    GpuNodeEntity getNodeByAddress(String address);
}
