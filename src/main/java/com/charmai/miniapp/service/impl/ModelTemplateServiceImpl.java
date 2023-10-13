package com.charmai.miniapp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charmai.common.exception.ServiceException;
import com.charmai.miniapp.constant.ExceptionConstant;
import com.charmai.miniapp.entity.ModelLoraEntity;
import com.charmai.miniapp.entity.ModelTemplateEntity;
import com.charmai.miniapp.entity.ModelTemplateTypeEntity;
import com.charmai.miniapp.mapper.ModelTemplateMapper;
import com.charmai.miniapp.service.ModelLoraService;
import com.charmai.miniapp.service.ModelTemplateService;
import com.charmai.miniapp.service.ModelTemplateTypeService;
import com.charmai.miniapp.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("modelTemplateService")
public class ModelTemplateServiceImpl extends ServiceImpl<ModelTemplateMapper, ModelTemplateEntity> implements ModelTemplateService {

    @Autowired
    ModelLoraService modelLoraService;

    @Autowired
    ModelTemplateTypeService modelTemplateTypeService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ModelTemplateEntity> page = this.page(
                new Query<ModelTemplateEntity>().getPage(params),
                new QueryWrapper<ModelTemplateEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public SearchTemplateResponseVo searchModelTemplates(SearchTemplateRequestVo searchTemplateRequestVo) {
        SearchTemplateResponseVo searchTemplateResponseVo = new SearchTemplateResponseVo();
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, String.valueOf(searchTemplateRequestVo.getPageNum()));
        params.put(Constant.LIMIT, String.valueOf(searchTemplateRequestVo.getPageSize()));
        QueryWrapper<ModelTemplateEntity> modelTemplateEntityQueryWrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(searchTemplateRequestVo.getTypeCue())) {
            modelTemplateEntityQueryWrapper.lambda().eq(ModelTemplateEntity::getTypeCue, searchTemplateRequestVo.getTypeCue());
        }
        IPage<ModelTemplateEntity> page = this.page(
                new Query<ModelTemplateEntity>().getPage(params),
                modelTemplateEntityQueryWrapper
        );
        List<ModelTemplateDto> modelTemplateDtos = new ArrayList<>();
        for (ModelTemplateEntity modelTemplateEntity : page.getRecords()) {
            ModelTemplateDto modelTemplateDto = new ModelTemplateDto();
            BeanUtils.copyProperties(modelTemplateEntity, modelTemplateDto);
            LambdaQueryWrapper<ModelLoraEntity> wrapper = new QueryWrapper<ModelLoraEntity>().lambda().eq(ModelLoraEntity::getTemplateId, modelTemplateEntity.getTemplateId());
            List<ModelLoraEntity> modelLoraEntities = modelLoraService.list(wrapper);
            modelTemplateDto.setModelLoraEntities(modelLoraEntities);
            modelTemplateDtos.add(modelTemplateDto);

        }
        searchTemplateResponseVo.setModelTemplates(modelTemplateDtos);
        searchTemplateResponseVo.setPageSize(page.getCurrent());
        searchTemplateResponseVo.setPageNum(page.getSize());
        return searchTemplateResponseVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertModelTemplateType(InsertModelTemplateRequestVo insertModelTemplateRequestVo) {

        if (ObjectUtils.isEmpty(insertModelTemplateRequestVo.getTypeCue())) {
            insertModelTemplateRequestVo.setTypeCue("default");
        }
        LambdaQueryWrapper<ModelTemplateTypeEntity> queryWrapper = new QueryWrapper<ModelTemplateTypeEntity>().lambda()
                .eq(ModelTemplateTypeEntity::getTemplateTypeKey, insertModelTemplateRequestVo.getTypeCue());
        ModelTemplateTypeEntity modelTemplateTypeEntity = modelTemplateTypeService.getOne(queryWrapper);
        if (ObjectUtils.isEmpty(modelTemplateTypeEntity)) {
            throw new ServiceException(ExceptionConstant.MODEL_TEMPLATE_TYPE_NOT_EXIT_EXCEPTION_MSG,
                    ExceptionConstant.MODEL_TEMPLATE_TYPE_NOT_EXIT_EXCEPTION_CODE);
        }

        ModelTemplateEntity modelTemplateEntity = new ModelTemplateEntity();
        BeanUtils.copyProperties(insertModelTemplateRequestVo, modelTemplateEntity);
        save(modelTemplateEntity);

        if (!CollectionUtils.isEmpty(insertModelTemplateRequestVo.getModelLoraEntities())) {
            for (ModelLoraEntity modelLoraEntity : insertModelTemplateRequestVo.getModelLoraEntities()) {
                modelLoraEntity.setTemplateId(modelTemplateEntity.getTemplateId());
            }
            modelLoraService.saveBatch(insertModelTemplateRequestVo.getModelLoraEntities());
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateModelTemplate(UpdateModelTemplateRequestVo updateModelTemplateRequestVo) {


        Long templateId = updateModelTemplateRequestVo.getTemplateId();
        ModelTemplateEntity modelTemplateEntity = baseMapper.selectById(templateId);
        if (ObjectUtils.isEmpty(modelTemplateEntity)) {
            throw new ServiceException(ExceptionConstant.MODEL_TEMPLATE_NOT_EXIT_EXCEPTION_MSG,
                    ExceptionConstant.MODEL_TEMPLATE_NOT_EXIT_EXCEPTION_CODE);
        }


        //校验模板分类的key
        if (!ObjectUtils.isEmpty(updateModelTemplateRequestVo.getTypeCue())) {
            LambdaQueryWrapper<ModelTemplateTypeEntity> queryWrapper = new QueryWrapper<ModelTemplateTypeEntity>().lambda()
                    .eq(ModelTemplateTypeEntity::getTemplateTypeKey, updateModelTemplateRequestVo.getTypeCue());
            ModelTemplateTypeEntity modelTemplateTypeEntity = modelTemplateTypeService.getOne(queryWrapper);
            if (ObjectUtils.isEmpty(modelTemplateTypeEntity)) {
                throw new ServiceException(ExceptionConstant.MODEL_TEMPLATE_TYPE_NOT_EXIT_EXCEPTION_MSG,
                        ExceptionConstant.MODEL_TEMPLATE_TYPE_NOT_EXIT_EXCEPTION_CODE);
            }
        }
        BeanUtils.copyProperties(updateModelTemplateRequestVo, modelTemplateEntity);
        baseMapper.updateById(modelTemplateEntity);

        //更新modelRola
        if (!CollectionUtils.isEmpty(updateModelTemplateRequestVo.getModelLoraEntities())) {
            for (ModelLoraEntity modelLoraEntity : updateModelTemplateRequestVo.getModelLoraEntities()) {
                modelLoraEntity.setTemplateId(templateId);
                modelLoraService.saveOrUpdate(modelLoraEntity);
            }
        }


    }


}