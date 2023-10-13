package com.charmai.miniapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charmai.miniapp.entity.LoraEntity;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface LoraInfoMapper extends BaseMapper<LoraEntity> {
    List<LoraEntity> getLoraInfo(String template_id);
}
