package com.charmai.miniapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charmai.miniapp.entity.WxUserInviteCodeEntity;
import com.charmai.miniapp.entity.WxUserInviteCodeRecordEntity;
import com.charmai.miniapp.mapper.WxUserInviteCodeRecordMapper;
import com.charmai.miniapp.service.WxUserInviteCodeRecordService;
import com.charmai.miniapp.service.WxUserInviteCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;


@Service("wxUserInviteCodeRecordService")
public class WxUserInviteCodeRecordServiceImpl extends ServiceImpl<WxUserInviteCodeRecordMapper, WxUserInviteCodeRecordEntity> implements WxUserInviteCodeRecordService {

    @Autowired
    WxUserInviteCodeService wxUserInviteCodeService;

    @Override
    public void saveWxUserInviteCodeRecord(String code, String wxInviteeUserId) {
        /**
         * 1.判断邀请码是否存在
         *    1.1 邀请码存在，判断被邀请用户是否存在被邀请记录
         *          1.1.1 被邀请用户不存在被邀请记录，直接保存对应的要邀请记录
         */
        WxUserInviteCodeRecordEntity wxUserInviteCodeRecordEntity;
        WxUserInviteCodeEntity wxUserInviteCodeEntity = wxUserInviteCodeService.getWxUserInviteCodeEntity(code);
        if (!ObjectUtils.isEmpty(wxUserInviteCodeEntity)) {
            wxUserInviteCodeRecordEntity = baseMapper.selectOne(new QueryWrapper<WxUserInviteCodeRecordEntity>().eq("wx_invitee_user_id", wxInviteeUserId));
            if (ObjectUtils.isEmpty(wxUserInviteCodeRecordEntity)) {
                wxUserInviteCodeRecordEntity.setInviteCode(code);
                wxUserInviteCodeRecordEntity.setWxInviterUserId(wxUserInviteCodeEntity.getWxUserId());
                wxUserInviteCodeRecordEntity.setWxInviteeUserId(wxInviteeUserId);
                save(wxUserInviteCodeRecordEntity);
            }
        }
    }


}