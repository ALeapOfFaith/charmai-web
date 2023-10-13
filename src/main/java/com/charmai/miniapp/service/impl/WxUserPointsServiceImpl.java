package com.charmai.miniapp.service.impl;

import com.charmai.common.core.domain.R;
import com.charmai.common.exception.ServiceException;
import com.charmai.miniapp.constant.ExceptionConstant;
import com.charmai.miniapp.constant.RedisConstant;
import com.charmai.miniapp.constant.WxUserPointsConstant;
import com.charmai.miniapp.entity.TaskEntity;
import com.charmai.miniapp.entity.WxUserInviteCodeRecordEntity;
import com.charmai.miniapp.entity.WxUserPointsDetailEntity;
import com.charmai.miniapp.entity.WxUserPointsEntity;
import com.charmai.miniapp.mapper.WxUserPointsMapper;
import com.charmai.miniapp.service.TaskService;
import com.charmai.miniapp.service.WxUserInviteCodeRecordService;
import com.charmai.miniapp.service.WxUserPointsDetailService;
import com.charmai.miniapp.service.WxUserPointsService;
import com.charmai.miniapp.utils.ConstantUtils;
import com.charmai.miniapp.utils.RedisClientUtils;
import com.charmai.miniapp.vo.IncreasePointsRequestVo;
import com.charmai.miniapp.vo.WxUserPointsResponseVo;
import com.charmai.weixin.constant.MyReturnCode;
import com.charmai.weixin.entity.WxUser;
import com.charmai.weixin.service.WxUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Slf4j
@Service("wxUserPointsService")
public class WxUserPointsServiceImpl extends ServiceImpl<WxUserPointsMapper, WxUserPointsEntity> implements WxUserPointsService {

    @Autowired
    WxUserService wxUserService;

    @Autowired
    WxUserPointsDetailService wxUserPointsDetailService;

    @Autowired
    WxUserInviteCodeRecordService wxUserInviteCodeRecordService;
    @Autowired
    TaskService taskService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WxUserPointsEntity> page = this.page(
                new Query<WxUserPointsEntity>().getPage(params),
                new QueryWrapper<WxUserPointsEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public WxUserPointsResponseVo getWxUserTotalPoints(String userId) {
        WxUserPointsResponseVo wxUserPointsResponseVo = new WxUserPointsResponseVo();
        /**
         * 1.判断用户是否存在
         * 2.判断用户积分记录是否存在
         *  2.1 不存在直接返回0积分
         *
         */

        WxUser wxUser = wxUserService.getById(userId);
        if (ObjectUtils.isEmpty(wxUser)) {
            throw new ServiceException(ExceptionConstant.USER_NOT_EXIT_EXCEPTION_MSG, ExceptionConstant.USER_NOT_EXIT_EXCEPTION_CODE);
        }
        WxUserPointsEntity wxUserPoints = baseMapper.selectOne(new QueryWrapper<WxUserPointsEntity>().eq("wx_user_id", userId));
        if (!ObjectUtils.isEmpty(wxUserPoints)) {
            wxUserPointsResponseVo.setTotalPoints(wxUserPoints.getTotalPoints());
        } else {
            wxUserPointsResponseVo.setTotalPoints(0L);
        }
        return wxUserPointsResponseVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void increaseWxUserPoints(String userId, long points, String outTradeNo, Integer increaseType) {
        /**
         *  1.判断用户是否存在
         *  2.新增积分详情
         *  3.计算用户总积分
         *      3.1 判断用户积分记录是否存在
         *          3.1 不存在，新增用户积分记录
         *          3.2 存在，计算用户总积分
         */
        WxUser wxUser = wxUserService.getById(userId);
        if (ObjectUtils.isEmpty(wxUser)) {
            throw new ServiceException(ExceptionConstant.USER_NOT_EXIT_EXCEPTION_MSG, ExceptionConstant.USER_NOT_EXIT_EXCEPTION_CODE);
        }
        String lockName = RedisConstant.WX_USER_POINTS_LOCK_NAME + userId;
        long waitTime = 200;

        if (!RedisClientUtils.tryLock(lockName, waitTime, TimeUnit.MILLISECONDS)) {
            RedisClientUtils.unLock(lockName);
            log.error("increaseWxUserPoints repeat submit exception,wx_user_id: {} ", userId);
            throw new ServiceException(ExceptionConstant.USER_POINTS_REPEAT_SUBMIT_EXCEPTION_MSG,
                    ExceptionConstant.USER_POINTS_REPEAT_SUBMIT_EXCEPTION_CODE);
        }

        try {
            //新增积分详情
            WxUserPointsDetailEntity wxUserPointsDetailEntity = new WxUserPointsDetailEntity();
            wxUserPointsDetailEntity.setWxUserId(userId);
            wxUserPointsDetailEntity.setPoint(points);
            wxUserPointsDetailEntity.setOutTradeNo(outTradeNo);
            wxUserPointsDetailEntity.setType(WxUserPointsConstant.INCREASE);
            wxUserPointsDetailEntity.setIncreaseType(increaseType);
            wxUserPointsDetailService.save(wxUserPointsDetailEntity);

            WxUserPointsEntity wxUserPoints = baseMapper.selectOne(new QueryWrapper<WxUserPointsEntity>().eq("wx_user_id", userId));
            if (ObjectUtils.isEmpty(wxUserPoints)) {
                wxUserPoints = new WxUserPointsEntity();
                wxUserPoints.setWxUserId(userId);
                wxUserPoints.setTotalPoints(points);
                save(wxUserPoints);
            } else {
                Long totalPoints = wxUserPoints.getTotalPoints();
                wxUserPoints.setTotalPoints(totalPoints + points);
                updateById(wxUserPoints);
            }

        } finally {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    log.info("事务提交成功,lockName: {} ,status:{} ", lockName, status);
                    RedisClientUtils.unLock(lockName);
                }
            });
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deductWxUserPoints(String userId, long points) {
        /**
         *  1.判断用户是否存在
         *  2.判断积分是否满足
         *      2.1 不满足抛出积分不足异常
         *      2.2 满足，新增积分详情，计算用户总积分
         */
        WxUser wxUser = wxUserService.getById(userId);
        if (ObjectUtils.isEmpty(wxUser)) {
            throw new ServiceException(ExceptionConstant.USER_NOT_EXIT_EXCEPTION_MSG, ExceptionConstant.USER_NOT_EXIT_EXCEPTION_CODE);
        }

        String lockName = RedisConstant.WX_USER_POINTS_LOCK_NAME + userId;
        long waitTime = 200;

        if (!RedisClientUtils.tryLock(lockName, waitTime, TimeUnit.MILLISECONDS)) {
            RedisClientUtils.unLock(lockName);
            log.error("deductWxUserPoints repeat submit exception,wx_user_id: {} ", userId);
            throw new ServiceException(ExceptionConstant.USER_POINTS_REPEAT_SUBMIT_EXCEPTION_MSG,
                    ExceptionConstant.USER_POINTS_REPEAT_SUBMIT_EXCEPTION_CODE);
        }

        try {

            WxUserPointsEntity wxUserPoints = baseMapper.selectOne(new QueryWrapper<WxUserPointsEntity>().eq("wx_user_id", userId));

            if (ObjectUtils.isEmpty(wxUserPoints)
                    || wxUserPoints.getTotalPoints() - points < 0) {
                throw new ServiceException(ExceptionConstant.USER_POINTS_NOT_ENOUGH_SUBMIT_EXCEPTION_MSG,
                        ExceptionConstant.USER_POINTS_NOT_ENOUGH_EXCEPTION_CODE);
            }
            //新增积分记录,计算用户总积分
            WxUserPointsDetailEntity wxUserPointsDetailEntity = new WxUserPointsDetailEntity();
            wxUserPointsDetailEntity.setWxUserId(userId);
            wxUserPointsDetailEntity.setPoint(points);
            wxUserPointsDetailEntity.setType(WxUserPointsConstant.DEDUCT);
            wxUserPointsDetailService.save(wxUserPointsDetailEntity);
            wxUserPoints.setTotalPoints(wxUserPoints.getTotalPoints() - points);
            updateById(wxUserPoints);


        } finally {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    log.info("事务提交成功,lockName: {} ,status:{} ", lockName, status);
                    RedisClientUtils.unLock(lockName);
                }
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void operateIncreasePoints(IncreasePointsRequestVo increasePointsRequest) {

        WxUser wxUser = wxUserService.getById(increasePointsRequest.getUserId());
        if (ObjectUtils.isEmpty(wxUser)) {
            throw new ServiceException(ExceptionConstant.USER_NOT_EXIT_EXCEPTION_MSG, ExceptionConstant.USER_NOT_EXIT_EXCEPTION_CODE);
        }
        if (ConstantUtils.INCREASE_TYPE_RECHARGE == increasePointsRequest.getIncreaseType()) {
            WxUserInviteCodeRecordEntity wxInviteeUserRecord = wxUserInviteCodeRecordService.getOne(new QueryWrapper<WxUserInviteCodeRecordEntity>().eq("wx_invitee_user_id", wxUser.getId()));
            if (!ObjectUtils.isEmpty(wxInviteeUserRecord)) {
                //邀请人增加积分
                increaseWxUserPoints(wxInviteeUserRecord.getWxInviterUserId(), ConstantUtils.INVITE_USER_POINTS, null, null);
            }
            QueryWrapper<WxUserPointsDetailEntity> wxUserPointsDetailEntityQueryWrapper = new QueryWrapper<WxUserPointsDetailEntity>().eq("wx_user_id", wxUser.getId())
                    .eq("increase_type", ConstantUtils.INCREASE_TYPE_RECHARGE);
            List<WxUserPointsDetailEntity> wxUserPointsDetailEntities = wxUserPointsDetailService.list(wxUserPointsDetailEntityQueryWrapper);
            // 没有充值记录，首次ios充值会启动数字分身任务
            if (CollectionUtils.isEmpty(wxUserPointsDetailEntities)) {
                TaskEntity taskEntity = taskService.queryTaskByUserId(increasePointsRequest.getUserId());
                if( taskEntity == null){
                    taskService.digitalIdentifierGenerate(wxUser.getId());
                }
            }
        }
        //增加积分
        increaseWxUserPoints(wxUser.getId(), increasePointsRequest.getPoints(), null, increasePointsRequest.getIncreaseType());
    }

}