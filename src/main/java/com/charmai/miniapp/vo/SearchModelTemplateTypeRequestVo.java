package com.charmai.miniapp.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: Xie
 * @Date: 2023-08-06-13:51
 * @Description:
 */
@Data
public class SearchModelTemplateTypeRequestVo {

    private int pageSize;

    private int pageNum;
}
