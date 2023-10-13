package com.charmai.miniapp.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.charmai.miniapp.entity.ModelTemplateEntity;
import com.charmai.miniapp.service.impl.PageUtils;
import com.charmai.miniapp.vo.InsertModelTemplateRequestVo;
import com.charmai.miniapp.vo.SearchTemplateRequestVo;
import com.charmai.miniapp.vo.SearchTemplateResponseVo;
import com.charmai.miniapp.vo.UpdateModelTemplateRequestVo;

import java.util.Map;

/**
 * 
 *
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-08-06 12:36:58
 */
public interface ModelTemplateService extends IService<ModelTemplateEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateModelTemplate(UpdateModelTemplateRequestVo updateModelTemplateRequestVo);

    SearchTemplateResponseVo searchModelTemplates(SearchTemplateRequestVo searchTemplateRequestVo);

    void insertModelTemplateType(InsertModelTemplateRequestVo insertModelTemplateRequestVo);
}

