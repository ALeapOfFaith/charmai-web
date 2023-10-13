package com.charmai.miniapp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.charmai.miniapp.entity.WxUserInviteCodeEntity;
import com.charmai.miniapp.vo.WxUserInviteCodeResponseVo;

/**
 * @Author: Xie
 * @Date: 2023-07-28-23:04
 * @Description: 邀请码接口
 */
public interface WxUserInviteCodeService extends IService<WxUserInviteCodeEntity> {

    /**
     * 生成邀请码
     *
     * @param userId
     * @return
     */
    WxUserInviteCodeResponseVo getInviteCode(String userId);

    /**
     * 获取校验码详情
     *
     * @param code
     * @return 邀请码详情
     */
    WxUserInviteCodeEntity getWxUserInviteCodeEntity(String code);

    /**
     * 校验邀请码是否有效
     *
     * @param code
     * @return
     */
     WxUserInviteCodeEntity checkInviteCode(String code);

}

