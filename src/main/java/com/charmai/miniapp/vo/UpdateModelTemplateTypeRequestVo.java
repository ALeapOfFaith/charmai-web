package com.charmai.miniapp.vo;

import com.charmai.miniapp.entity.ModelTemplateTypeEntity;
import lombok.Data;

/**
 * @Author: Xie
 * @Date: 2023-08-06-16:27
 * @Description:
 */
@Data
public class UpdateModelTemplateTypeRequestVo {

    private Long templateTypeId;

    private String templateTypeName;

    private String templateTypeKey;
}
