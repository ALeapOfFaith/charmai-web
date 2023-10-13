package com.charmai.miniapp.vo;

import com.charmai.miniapp.entity.ModelLoraEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Author: Xie
 * @Date: 2023-08-06-17:24
 * @Description:
 */
@Data
public class InsertModelTemplateRequestVo {


    /**
     * 模板名称
     */
    private String templateName;
    /**
     * 模板描述
     */
    private String templateDescription;
    /**
     * SD训练通用参数模板映射
     */
    private String templateMapping;
    /**
     * 小图
     */
    private String coverUrl;
    /**
     * 大图
     */
    private String picUrl;
    /**
     * 分类key
     */
    @NotBlank
    private String typeCue;
    /**
     * 使用次数
     */
    private Integer used;

    List<ModelLoraEntity> modelLoraEntities;



}
