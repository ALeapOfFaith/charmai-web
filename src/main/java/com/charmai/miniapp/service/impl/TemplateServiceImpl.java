package com.charmai.miniapp.service.impl;

import com.charmai.miniapp.entity.TemplateDto;
import com.charmai.miniapp.entity.TemplateEntity;
import com.charmai.miniapp.mapper.TemplateMapper;
import com.charmai.miniapp.service.TemplateService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("templateService")
public class TemplateServiceImpl implements TemplateService {
    final
    TemplateMapper templateMapper;

    public TemplateServiceImpl(TemplateMapper templateMapper) {
        this.templateMapper = templateMapper;
    }

    @Override
    public List<TemplateEntity> searchTemplate(String type_cue) {
        return templateMapper.searchTemplate(type_cue);
    }

    @Override
    public TemplateEntity getTemplateById(String templateId) {
        return templateMapper.getTemplateById(templateId);
    }
}
