package com.charmai.miniapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charmai.common.exception.ServiceException;
import com.charmai.miniapp.constant.ExceptionConstant;
import com.charmai.miniapp.entity.ModelTemplateEntity;
import com.charmai.miniapp.entity.ModelTemplateTypeEntity;
import com.charmai.miniapp.mapper.ModelTemplateTypeMapper;
import com.charmai.miniapp.service.ModelTemplateService;
import com.charmai.miniapp.service.ModelTemplateTypeService;
import com.charmai.miniapp.vo.InsertModelTemplateTypeRequestVo;
import com.charmai.miniapp.vo.SearchModelTemplateTypeRequestVo;
import com.charmai.miniapp.vo.SearchModelTemplateTypeResponseVo;
import com.charmai.miniapp.vo.UpdateModelTemplateTypeRequestVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;


@Service("modelTemplateTypeService")
public class ModelTemplateTypeServiceImpl extends ServiceImpl<ModelTemplateTypeMapper, ModelTemplateTypeEntity> implements ModelTemplateTypeService {

    @Autowired
    ModelTemplateService modelTemplateService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ModelTemplateTypeEntity> page = this.page(
                new Query<ModelTemplateTypeEntity>().getPage(params),
                new QueryWrapper<ModelTemplateTypeEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public SearchModelTemplateTypeResponseVo searchModelTemplateTypes(SearchModelTemplateTypeRequestVo searchModelTemplateTypeRequestVo) {
        SearchModelTemplateTypeResponseVo searchModelTemplateTypeResponseVo = new SearchModelTemplateTypeResponseVo();
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, String.valueOf(searchModelTemplateTypeRequestVo.getPageNum()));
        params.put(Constant.LIMIT, String.valueOf(searchModelTemplateTypeRequestVo.getPageSize()));
        IPage<ModelTemplateTypeEntity> page = this.page(
                new Query<ModelTemplateTypeEntity>().getPage(params),
                new QueryWrapper<ModelTemplateTypeEntity>()
        );

        searchModelTemplateTypeResponseVo.setModelTemplateTypeEntities(page.getRecords());
        searchModelTemplateTypeResponseVo.setTotal(page.getTotal());
        searchModelTemplateTypeResponseVo.setPageSize(page.getSize());
        searchModelTemplateTypeResponseVo.setPageNum(page.getPages());
        return searchModelTemplateTypeResponseVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertModelTemplateType(InsertModelTemplateTypeRequestVo insertModelTemplateTypeRequestVo) {
        checkTemplateTypeKey(insertModelTemplateTypeRequestVo.getTemplateTypeKey());
        ModelTemplateTypeEntity modelTemplateTypeEntity = new ModelTemplateTypeEntity();
        modelTemplateTypeEntity.setTemplateTypeName(insertModelTemplateTypeRequestVo.getTemplateTypeName());
        modelTemplateTypeEntity.setTemplateTypeKey(insertModelTemplateTypeRequestVo.getTemplateTypeKey());
        save(modelTemplateTypeEntity);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateModelTemplateType(UpdateModelTemplateTypeRequestVo updateModelTemplateTypeRequestVo) {

        checkTemplateTypeKey(updateModelTemplateTypeRequestVo.getTemplateTypeKey());

        ModelTemplateTypeEntity modelTemplateTypeEntity = getById(updateModelTemplateTypeRequestVo.getTemplateTypeId());
        if (ObjectUtils.isEmpty(modelTemplateTypeEntity)) {
            throw new ServiceException(ExceptionConstant.MODEL_TEMPLATE_TYPE_NOT_EXIT_EXCEPTION_MSG,
                    ExceptionConstant.MODEL_TEMPLATE_TYPE_NOT_EXIT_EXCEPTION_CODE);
        }

        /**
         * 修改模板的值
         */
        UpdateWrapper<ModelTemplateEntity> updateWrapper = new UpdateWrapper<ModelTemplateEntity>();
        updateWrapper.lambda().eq(ModelTemplateEntity::getTypeCue, updateModelTemplateTypeRequestVo.getTemplateTypeKey())
                .set(ModelTemplateEntity::getTypeCue, updateModelTemplateTypeRequestVo.getTemplateTypeKey());
        modelTemplateService.update(updateWrapper);

        /**
         * 修改模板类型
         */
        BeanUtils.copyProperties(updateModelTemplateTypeRequestVo, modelTemplateTypeEntity);
        updateById(modelTemplateTypeEntity);


    }


    @Override
    public void checkTemplateTypeKey(String templateTypeKey) {

        QueryWrapper<ModelTemplateTypeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModelTemplateTypeEntity::getTemplateTypeKey, templateTypeKey);
        ModelTemplateTypeEntity modelTemplateTypeEntity = getOne(queryWrapper);
        if (!ObjectUtils.isEmpty(modelTemplateTypeEntity)) {
            throw new ServiceException(ExceptionConstant.MODEL_TEMPLATE_TYPE_ALREADY_EXIT_EXCEPTION_MSG,
                    ExceptionConstant.MODEL_TEMPLATE_TYPE_ALREADY_EXIT_EXCEPTION_CODE);
        }
    }


}