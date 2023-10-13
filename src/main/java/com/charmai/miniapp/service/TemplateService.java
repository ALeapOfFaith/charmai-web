package com.charmai.miniapp.service;

import com.charmai.miniapp.entity.TemplateDto;
import com.charmai.miniapp.entity.TemplateEntity;

import java.util.List;

public interface TemplateService {
    List<TemplateEntity >searchTemplate(String type_cue);

    public TemplateEntity getTemplateById(String templateId);
}
