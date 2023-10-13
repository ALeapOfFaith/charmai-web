package com.charmai.miniapp.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: Xie
 * @Date: 2023-08-06-13:14
 * @Description:
 */
@Data
public class SearchTemplateRequestVo {

    private String typeCue;
    @NotNull
    private int pageSize;
    @NotNull
    private int pageNum;

}
