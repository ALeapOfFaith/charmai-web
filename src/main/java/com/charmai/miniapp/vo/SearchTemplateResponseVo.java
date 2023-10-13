package com.charmai.miniapp.vo;


import lombok.Data;

import java.util.List;

/**
 * @Author: Xie
 * @Date: 2023-08-06-13:16
 * @Description:
 */
@Data
public class SearchTemplateResponseVo {

    private List<ModelTemplateDto> modelTemplates;
    private Long Total;
    private Long pageSize;
    private Long pageNum;

}
