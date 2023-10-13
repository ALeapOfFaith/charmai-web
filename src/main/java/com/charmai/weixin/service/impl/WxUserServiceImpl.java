/*
MIT License

Copyright (c) 2020 www.joolun.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.charmai.weixin.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charmai.common.exception.ServiceException;
import com.charmai.miniapp.constant.ExceptionConstant;
import com.charmai.miniapp.entity.WxUserInviteCodeEntity;
import com.charmai.miniapp.entity.WxUserInviteCodeRecordEntity;
import com.charmai.miniapp.entity.WxUserPayRecordEntity;
import com.charmai.miniapp.entity.WxUserPointsDetailEntity;
import com.charmai.miniapp.mapper.WxUserInviteCodeMapper;
import com.charmai.miniapp.service.WxUserInviteCodeRecordService;
import com.charmai.miniapp.service.WxUserInviteCodeService;
import com.charmai.miniapp.service.WxUserPayRecordService;
import com.charmai.miniapp.service.WxUserPointsDetailService;
import com.charmai.miniapp.utils.ConstantUtils;
import com.charmai.weixin.entity.LoginMaDTO;
import com.google.common.collect.Lists;
import com.charmai.weixin.config.WxMaConfiguration;
import com.charmai.weixin.constant.ConfigConstant;
import com.charmai.weixin.constant.WxMaConstants;
import com.charmai.weixin.entity.ThirdSession;
import com.charmai.weixin.entity.WxOpenDataDTO;
import com.charmai.weixin.entity.WxUser;
import com.charmai.weixin.handler.SubscribeHandler;
import com.charmai.weixin.mapper.WxUserMapper;
import com.charmai.weixin.service.WxUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import me.chanjar.weixin.mp.api.WxMpUserTagService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 微信用户
 *
 * @author www.joolun.com
 * @date 2019-03-25 15:39:39
 */
@Slf4j
@Service
@AllArgsConstructor
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements WxUserService {

    private final WxMpService wxService;
    private final RedisTemplate redisTemplate;

    @Autowired
    WxUserInviteCodeService wxUserInviteCodeService;

    @Autowired
    WxUserInviteCodeRecordService wxUserInviteCodeRecordService;

    @Autowired
    WxUserPayRecordService wxUserPayRecordService;

    @Autowired
    WxUserPointsDetailService wxUserPointsDetailService;

    @Autowired
    WxUserInviteCodeMapper wxUserInviteCodeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRemark(WxUser entity) throws WxErrorException {
        String id = entity.getId();
        String remark = entity.getRemark();
        String openId = entity.getOpenId();
        entity = new WxUser();
        entity.setId(id);
        entity.setRemark(remark);
        super.updateById(entity);
        WxMpUserService wxMpUserService = wxService.getUserService();
        wxMpUserService.userUpdateRemark(openId, remark);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tagging(String taggingType, Long tagId, String[] openIds) throws WxErrorException {
        WxMpUserTagService wxMpUserTagService = wxService.getUserTagService();
        WxUser wxUser;
        if ("tagging".equals(taggingType)) {
            for (String openId : openIds) {
                wxUser = baseMapper.selectOne(Wrappers.<WxUser>lambdaQuery()
                        .eq(WxUser::getOpenId, openId));
                Long[] tagidList = wxUser.getTagidList();
                List<Long> list = Arrays.asList(tagidList);
                list = new ArrayList<>(list);
                if (!list.contains(tagId)) {
                    list.add(tagId);
                    tagidList = list.toArray(new Long[list.size()]);
                    wxUser.setTagidList(tagidList);
                    this.updateById(wxUser);
                }
            }
            wxMpUserTagService.batchTagging(tagId, openIds);
        }
        if ("unTagging".equals(taggingType)) {
            for (String openId : openIds) {
                wxUser = baseMapper.selectOne(Wrappers.<WxUser>lambdaQuery()
                        .eq(WxUser::getOpenId, openId));
                Long[] tagidList = wxUser.getTagidList();
                List<Long> list = Arrays.asList(tagidList);
                list = new ArrayList<>(list);
                if (list.contains(tagId)) {
                    list.remove(tagId);
                    tagidList = list.toArray(new Long[list.size()]);
                    wxUser.setTagidList(tagidList);
                    this.updateById(wxUser);
                }
            }
            wxMpUserTagService.batchUntagging(tagId, openIds);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void synchroWxUser() throws WxErrorException {
        //先将已关注的用户取关
        WxUser wxUser = new WxUser();
        wxUser.setSubscribe(ConfigConstant.SUBSCRIBE_TYPE_NO);
        this.baseMapper.update(wxUser, Wrappers.<WxUser>lambdaQuery()
                .eq(WxUser::getSubscribe, ConfigConstant.SUBSCRIBE_TYPE_YES));
        WxMpUserService wxMpUserService = wxService.getUserService();
        this.recursionGet(wxMpUserService, null);
    }

    /**
     * 递归获取
     *
     * @param nextOpenid
     */
    void recursionGet(WxMpUserService wxMpUserService, String nextOpenid) throws WxErrorException {
        WxMpUserList userList = wxMpUserService.userList(nextOpenid);
        List<WxUser> listWxUser = new ArrayList<>();
        List<WxMpUser> listWxMpUser = getWxMpUserList(wxMpUserService, userList.getOpenids());
        listWxMpUser.forEach(wxMpUser -> {
            WxUser wxUser = baseMapper.selectOne(Wrappers.<WxUser>lambdaQuery()
                    .eq(WxUser::getOpenId, wxMpUser.getOpenId()));
            if (wxUser == null) {//用户未存在
                wxUser = new WxUser();
                wxUser.setSubscribeNum(1);
            }
            SubscribeHandler.setWxUserValue(wxUser, wxMpUser);
            listWxUser.add(wxUser);
        });
        this.saveOrUpdateBatch(listWxUser);
        if (userList.getCount() >= 10000) {
            this.recursionGet(wxMpUserService, userList.getNextOpenid());
        }
    }

    /**
     * 分批次获取微信粉丝信息 每批100条
     *
     * @param wxMpUserService
     * @param openidsList
     * @return
     * @throws WxErrorException
     * @author
     */
    private List<WxMpUser> getWxMpUserList(WxMpUserService wxMpUserService, List<String> openidsList) throws WxErrorException {
        // 粉丝openid数量
        int count = openidsList.size();
        if (count <= 0) {
            return new ArrayList<>();
        }
        List<WxMpUser> list = Lists.newArrayList();
        List<WxMpUser> followersInfoList;
        int a = count % 100 > 0 ? count / 100 + 1 : count / 100;
        for (int i = 0; i < a; i++) {
            if (i + 1 < a) {
                log.debug("i:{},from:{},to:{}", i, i * 100, (i + 1) * 100);
                followersInfoList = wxMpUserService.userInfoList(openidsList.subList(i * 100, ((i + 1) * 100)));
                if (null != followersInfoList && !followersInfoList.isEmpty()) {
                    list.addAll(followersInfoList);
                }
            } else {
                log.debug("i:{},from:{},to:{}", i, i * 100, count - i * 100);
                followersInfoList = wxMpUserService.userInfoList(openidsList.subList(i * 100, count));
                if (null != followersInfoList && !followersInfoList.isEmpty()) {
                    list.addAll(followersInfoList);
                }
            }
        }
        log.debug("本批次获取微信粉丝数：", list.size());
        return list;
    }

    @Override
    public WxUser getByOpenId(String openId) {
        return this.baseMapper.selectOne(Wrappers.<WxUser>lambdaQuery()
                .eq(WxUser::getOpenId, openId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WxUser loginMa(String appId, LoginMaDTO loginMaDTO) throws WxErrorException {
        WxMaJscode2SessionResult jscode2session = WxMaConfiguration.getMaService(appId).jsCode2SessionInfo(loginMaDTO.getJsCode());
        WxUser wxUser = this.getByOpenId(jscode2session.getOpenid());

        if (wxUser == null) {
            //新增微信用户
            wxUser = new WxUser();
            wxUser.setAppType(ConfigConstant.WX_APP_TYPE_1);
            wxUser.setOpenId(jscode2session.getOpenid());
            wxUser.setSessionKey(jscode2session.getSessionKey());
            wxUser.setUnionId(jscode2session.getUnionid());
            try {
                this.save(wxUser);
            } catch (DuplicateKeyException e) {
                if (e.getMessage().contains("uk_appid_openid")) {
                    wxUser = this.getByOpenId(wxUser.getOpenId());
                }
            }
        } else {
            //更新SessionKey
            wxUser.setAppType(ConfigConstant.WX_APP_TYPE_1);
            wxUser.setOpenId(jscode2session.getOpenid());
            wxUser.setSessionKey(jscode2session.getSessionKey());
            wxUser.setUnionId(jscode2session.getUnionid());
            this.updateById(wxUser);
        }

        /**
         * 1.判断用户是否存在邀请记录
         *      1.1 存在直接跳过
         *      1.2 不存在，判断邀请码是否存在且非同一个用户，有效生成邀请记录
         */
        if (!StringUtils.isEmpty(loginMaDTO.getInviteCode())) {
            WxUserInviteCodeRecordEntity wxInviteeUserRecord = wxUserInviteCodeRecordService.getOne(new QueryWrapper<WxUserInviteCodeRecordEntity>().eq("wx_invitee_user_id", wxUser.getId()));
            if (ObjectUtils.isEmpty(wxInviteeUserRecord)) {
                log.info("without wxInviteeUserRecord,start to insert wxInviteeUserRecord");
//                WxUserInviteCodeEntity wxUserInviteCodeEntity = wxUserInviteCodeService.checkInviteCode(loginMaDTO.getInviteCode());
                WxUserInviteCodeEntity wxUserInviteCodeEntity = wxUserInviteCodeMapper.selectOne(new QueryWrapper<WxUserInviteCodeEntity>().eq("invite_code", loginMaDTO.getInviteCode()));
                if (!ObjectUtils.isEmpty(wxUserInviteCodeEntity)
                        && !wxUserInviteCodeEntity.getWxUserId().equals(wxUser.getId())) {
                    wxInviteeUserRecord = new WxUserInviteCodeRecordEntity();
                    wxInviteeUserRecord.setInviteCode(loginMaDTO.getInviteCode());
                    wxInviteeUserRecord.setWxInviterUserId(wxUserInviteCodeEntity.getWxUserId());
                    wxInviteeUserRecord.setWxInviteeUserId(wxUser.getId());
                    wxUserInviteCodeRecordService.save(wxInviteeUserRecord);
                }
            }
        }

        String thirdSessionKey = UUID.randomUUID().toString();
        ThirdSession thirdSession = new ThirdSession();
        thirdSession.setAppId(appId);
        thirdSession.setSessionKey(wxUser.getSessionKey());
        thirdSession.setWxUserId(wxUser.getId());
        thirdSession.setOpenId(wxUser.getOpenId());
        //将3rd_session和用户信息存入redis，并设置过期时间
        String key = WxMaConstants.THIRD_SESSION_BEGIN + ":" + thirdSessionKey;
        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(thirdSession), WxMaConstants.TIME_OUT_SESSION, TimeUnit.HOURS);
        wxUser.setSessionKey(thirdSessionKey);
        return wxUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WxUser saveOrUptateWxUser(WxOpenDataDTO wxOpenDataDTO) {
        WxMaUserService wxMaUserService = WxMaConfiguration.getMaService(wxOpenDataDTO.getAppId()).getUserService();
        WxMaUserInfo wxMaUserInfo = wxMaUserService.getUserInfo(wxOpenDataDTO.getSessionKey(), wxOpenDataDTO.getEncryptedData(), wxOpenDataDTO.getIv());
        WxUser wxUser = new WxUser();
        BeanUtil.copyProperties(wxMaUserInfo, wxUser);
        wxUser.setId(wxOpenDataDTO.getUserId());
        wxUser.setSex(wxMaUserInfo.getGender());
        wxUser.setHeadimgUrl(wxMaUserInfo.getAvatarUrl());
        baseMapper.updateById(wxUser);
        wxUser = baseMapper.selectById(wxUser.getId());
        return wxUser;
    }

    @Override
    public Boolean checkCompletePayment(String userId) {
        WxUser wxUser = getById(userId);
        if (org.springframework.util.ObjectUtils.isEmpty(wxUser)) {
            throw new ServiceException(ExceptionConstant.USER_NOT_EXIT_EXCEPTION_MSG, ExceptionConstant.USER_NOT_EXIT_EXCEPTION_CODE);
        }

        //校验用户是否完成支付（安卓校验支付订单，ios校验积分充值）
        QueryWrapper<WxUserPayRecordEntity> wxUserPayRecordEntityQueryWrapper = new QueryWrapper<WxUserPayRecordEntity>().eq("wx_user_id", wxUser.getId())
                .eq("trade_state", "SUCCESS");
        List<WxUserPayRecordEntity> wxUserPayRecordEntities = wxUserPayRecordService.list(wxUserPayRecordEntityQueryWrapper);
        if (!CollectionUtils.isEmpty(wxUserPayRecordEntities)) {
            return true;

        }

        QueryWrapper<WxUserPointsDetailEntity> wxUserPointsDetailEntityQueryWrapper = new QueryWrapper<WxUserPointsDetailEntity>().eq("wx_user_id", wxUser.getId())
                .eq("increase_type", ConstantUtils.INCREASE_TYPE_RECHARGE);
        List<WxUserPointsDetailEntity> wxUserPointsDetailEntities = wxUserPointsDetailService.list(wxUserPointsDetailEntityQueryWrapper);
        if (!CollectionUtils.isEmpty(wxUserPointsDetailEntities)) {
            return true;
        }
        return false;

    }
}
