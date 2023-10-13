package com.charmai.miniapp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.charmai.miniapp.entity.ModelTemplateTypeEntity;
import com.charmai.miniapp.service.impl.PageUtils;
import com.charmai.miniapp.vo.InsertModelTemplateTypeRequestVo;
import com.charmai.miniapp.vo.SearchModelTemplateTypeRequestVo;
import com.charmai.miniapp.vo.SearchModelTemplateTypeResponseVo;
import com.charmai.miniapp.vo.UpdateModelTemplateTypeRequestVo;

import java.util.Map;

/**
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-08-06 12:36:58
 */
public interface ModelTemplateTypeService extends IService<ModelTemplateTypeEntity> {

    PageUtils queryPage(Map<String, Object> params);


    public SearchModelTemplateTypeResponseVo searchModelTemplateTypes(SearchModelTemplateTypeRequestVo searchModelTemplateTypeRequestVo);

    void updateModelTemplateType(UpdateModelTemplateTypeRequestVo updateModelTemplateTypeRequestVo);

    void insertModelTemplateType(InsertModelTemplateTypeRequestVo insertModelTemplateTypeRequestVo);

    void checkTemplateTypeKey(String templateTypeKey);
}

