package com.charmai.miniapp.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: Xie
 * @Date: 2023-08-04-23:53
 * @Description:
 */
@Data
public class IncreasePointsRequestVo {

    @NotBlank
    private String userId;

    @NotNull
    private Long points;

    @NotNull
    private Integer increaseType;

}
