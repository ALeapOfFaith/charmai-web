package com.charmai.miniapp.vo;

import com.charmai.miniapp.entity.ModelTemplateTypeEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author: Xie
 * @Date: 2023-08-06-13:52
 * @Description:
 */
@Data
public class SearchModelTemplateTypeResponseVo {


    private List<ModelTemplateTypeEntity> modelTemplateTypeEntities;
    private Long Total;
    private Long pageSize;
    private Long pageNum;

}
