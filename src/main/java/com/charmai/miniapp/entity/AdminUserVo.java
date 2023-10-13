package com.charmai.miniapp.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserVo extends AdminUserEntity{
    private String token;
}
