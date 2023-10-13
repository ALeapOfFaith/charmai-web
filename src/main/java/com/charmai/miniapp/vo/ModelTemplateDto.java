package com.charmai.miniapp.vo;

import com.charmai.miniapp.entity.ModelLoraEntity;
import com.charmai.miniapp.entity.ModelTemplateEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author: Xie
 * @Date: 2023-08-06-15:16
 * @Description:
 */
@Data
public class ModelTemplateDto extends ModelTemplateEntity {

    List<ModelLoraEntity> modelLoraEntities;

}
