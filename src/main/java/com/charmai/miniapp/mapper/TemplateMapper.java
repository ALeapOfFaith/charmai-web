package com.charmai.miniapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charmai.miniapp.entity.SearchVo;
import com.charmai.miniapp.entity.TemplateDto;
import com.charmai.miniapp.entity.TemplateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface TemplateMapper extends BaseMapper<TemplateDto> {
    List<TemplateEntity> searchTemplate(String type_cue);
    TemplateEntity getTemplateById(String templateId);
}
