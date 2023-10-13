package com.charmai.miniapp.api;

import com.charmai.common.core.domain.R;
import com.charmai.miniapp.entity.SearchVo;
import com.charmai.miniapp.entity.TemplateEntity;
import com.charmai.miniapp.service.LoraService;
import com.charmai.miniapp.service.ModelTemplateTypeService;
import com.charmai.miniapp.service.TemplateService;
import com.charmai.miniapp.vo.SearchModelTemplateTypeRequestVo;
import com.charmai.miniapp.vo.SearchModelTemplateTypeResponseVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/weixin/api/ma/homepage")
@Tag(name = "首页API")
public class HomePageApi {
    TemplateService templateService;
    @PostMapping("/template")
    @Operation(summary = "获取模板信息")
    public R<List> search(@RequestBody SearchVo searchVo){
        String type_cue = searchVo.getCategory();
        List<TemplateEntity> templateInfo = templateService.searchTemplate(type_cue);
        return R.ok(templateInfo);
    }

    LoraService loraService;
    @GetMapping("/templateInfo")
    @Operation(summary = "获取模板详情")
    public R<List> getLoraInfo(@RequestParam(value = "templateId") String templateId){
        List loraInfo = loraService.getLoraInfo(templateId);
        return R.ok(loraInfo);
    }

    ModelTemplateTypeService modelTemplateTypeService;
    @PostMapping("/template/type")
    @Operation(summary = "分页获取模板分类信息")
    public R<SearchModelTemplateTypeResponseVo> getLoraInfo(@RequestBody SearchModelTemplateTypeRequestVo searchModelTemplateTypeRequestVo){
        SearchModelTemplateTypeResponseVo searchModelTemplateTypeResponseVo = modelTemplateTypeService.searchModelTemplateTypes(searchModelTemplateTypeRequestVo);
        return R.ok(searchModelTemplateTypeResponseVo);
    }

}
