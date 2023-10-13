package com.charmai.miniapp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.charmai.miniapp.entity.WxUserInviteCodeRecordEntity;

import java.util.Map;

/**
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-07-29 11:13:39
 */
public interface WxUserInviteCodeRecordService extends IService<WxUserInviteCodeRecordEntity> {


    public void saveWxUserInviteCodeRecord(String code, String wxInviteeUserId);

}

