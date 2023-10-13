package com.charmai.miniapp.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charmai.common.exception.ServiceException;
import com.charmai.miniapp.constant.ExceptionConstant;
import com.charmai.miniapp.entity.WxUserInviteCodeEntity;
import com.charmai.miniapp.mapper.WxUserInviteCodeMapper;
import com.charmai.miniapp.service.WxUserInviteCodeService;
import com.charmai.miniapp.vo.WxUserInviteCodeResponseVo;
import com.charmai.weixin.entity.WxUser;
import com.charmai.weixin.service.WxUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


@Slf4j
@Service("wxUserInviteCodeService")
public class WxUserInviteCodeServiceImpl extends ServiceImpl<WxUserInviteCodeMapper, WxUserInviteCodeEntity> implements WxUserInviteCodeService {

    @Autowired
    WxUserService wxUserService;


    /**
     * 用于随机选的数字
     */
    public static final String BASE_NUMBER = "0123456789";
    /**
     * 用于随机选的字符
     */
    public static final String BASE_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * 用于随机选的字符和数字
     */
    public static final String BASE_CHAR_NUMBER = BASE_CHAR + BASE_NUMBER;

    @Override
    public WxUserInviteCodeResponseVo getInviteCode(String userId) {
        WxUserInviteCodeResponseVo wxUserInviteCodeResponseVo = new WxUserInviteCodeResponseVo();
        /**
         * 1.判断用户是否存在
         * 2.判断邀请码是否生成过邀请码
         *  2.1 生成过直接返回
         *  2.2 没有生成一个6为的邀请码(最多可能生成56.8亿)
         *      2.2.1 如果不存在相同的邀请码，重新生成
         *      2.2.2 如果存在，继续生成，知道不重复为止
         */
        String code;
        WxUser wxUser = wxUserService.getById(userId);
        if (ObjectUtils.isEmpty(wxUser)) {
            throw new ServiceException(ExceptionConstant.USER_NOT_EXIT_EXCEPTION_MSG, ExceptionConstant.USER_NOT_EXIT_EXCEPTION_CODE);
        }
        WxUserInviteCodeEntity wxUserInviteCode = baseMapper.selectOne(new QueryWrapper<WxUserInviteCodeEntity>().eq("wx_user_id", userId));
        if (!ObjectUtils.isEmpty(wxUserInviteCode)) {
            code = wxUserInviteCode.getInviteCode();
        } else {
            while (true) {
                code = RandomUtil.randomString(BASE_CHAR_NUMBER, 6);
                WxUserInviteCodeEntity inviteCode = baseMapper.selectOne(new QueryWrapper<WxUserInviteCodeEntity>().eq("invite_code", code));
                if (ObjectUtils.isEmpty(inviteCode)) {
                    WxUserInviteCodeEntity wxUserInviteCodeEntity = new WxUserInviteCodeEntity();
                    wxUserInviteCodeEntity.setWxUserId(userId);
                    wxUserInviteCodeEntity.setInviteCode(code);
                    save(wxUserInviteCodeEntity);
                    break;
                }
            }
        }
        wxUserInviteCodeResponseVo.setCode(code);
        return wxUserInviteCodeResponseVo;
    }

    @Override
    public WxUserInviteCodeEntity getWxUserInviteCodeEntity(String code) {
        WxUserInviteCodeEntity inviteCode = baseMapper.selectOne(new QueryWrapper<WxUserInviteCodeEntity>().eq("invite_code", code));
        if (!ObjectUtils.isEmpty(inviteCode)) {
            return inviteCode;
        }
        return null;
    }

    @Override
    public WxUserInviteCodeEntity checkInviteCode(String code) {
        WxUserInviteCodeEntity inviteCode = baseMapper.selectOne(new QueryWrapper<WxUserInviteCodeEntity>().eq("invite_code", code));
        if (!ObjectUtils.isEmpty(inviteCode)) {
            return inviteCode;
        } else {
            log.error("wx user invite code invalid inviteCode:{}", code);
            throw new ServiceException(ExceptionConstant.WX_USER_INVITE_CODE_INVALID_EXCEPTION_MSG, ExceptionConstant.WX_USER_INVITE_CODE_INVALID_EXCEPTION_CODE);
        }
    }

}