package com.charmai.miniapp.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.charmai.common.core.domain.R;
import com.charmai.miniapp.service.ModelTemplateService;
import com.charmai.miniapp.service.ModelTemplateTypeService;
import com.charmai.miniapp.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: Xie
 * @Date: 2023-08-06-11:51
 * @Description:
 */
@RestController
@RequestMapping("/weixin/api/operate")
public class OperateController {

    @Autowired
    ModelTemplateService modelTemplateService;

    @Autowired
    ModelTemplateTypeService modelTemplateTypeService;


    @PostMapping("/model/templates")
    @Operation(summary = "分页获取模板信息")
    @SaCheckLogin
    public R<SearchTemplateResponseVo> searchModelTemplates(@RequestBody SearchTemplateRequestVo searchTemplateRequestVo) {
        SearchTemplateResponseVo searchTemplateResponseVo = modelTemplateService.searchModelTemplates(searchTemplateRequestVo);
        return R.ok(searchTemplateResponseVo);
    }

    @PostMapping("/model/template/insert")
    @Operation(summary = "新增模板信息")
    public R insertModelTemplateType(@RequestBody InsertModelTemplateRequestVo insertModelTemplateRequestVo) {
        modelTemplateService.insertModelTemplateType(insertModelTemplateRequestVo);
        return R.ok();
    }


    @PostMapping("/model/template/update")
    @Operation(summary = "修改模板信息")
    public R updateModelTemplate(@RequestBody UpdateModelTemplateRequestVo updateModelTemplateRequestVo) {
        modelTemplateService.updateModelTemplate(updateModelTemplateRequestVo);
        return R.ok();
    }


    @PostMapping("/model/template/types")
    @Operation(summary = "分页获取模板分类信息")
    public R<SearchModelTemplateTypeResponseVo> getModelTemplateType(@RequestBody SearchModelTemplateTypeRequestVo searchModelTemplateTypeRequestVo) {
        SearchModelTemplateTypeResponseVo searchModelTemplateTypeResponseVo = modelTemplateTypeService.searchModelTemplateTypes(searchModelTemplateTypeRequestVo);
        return R.ok(searchModelTemplateTypeResponseVo);
    }

    @PostMapping("/model/template/type/insert")
    @Operation(summary = "新增模板分类信息")
    public R insertModelTemplateType(@RequestBody InsertModelTemplateTypeRequestVo insertModelTemplateTypeRequestVo) {
        modelTemplateTypeService.insertModelTemplateType(insertModelTemplateTypeRequestVo);
        return R.ok();
    }

    @PostMapping("/model/template/type/update")
    @Operation(summary = "修改模板分类信息")
    public R updateModelTemplateType(@RequestBody UpdateModelTemplateTypeRequestVo updateModelTemplateTypeRequestVo) {
        modelTemplateTypeService.updateModelTemplateType(updateModelTemplateTypeRequestVo);
        return R.ok();
    }














}
