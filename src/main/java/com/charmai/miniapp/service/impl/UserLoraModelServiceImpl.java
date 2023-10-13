package com.charmai.miniapp.service.impl;

import com.charmai.miniapp.mapper.UserLoraModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charmai.miniapp.entity.UserLoraModelEntity;
import com.charmai.miniapp.service.UserLoraModelService;


@Service("userLoraModelService")
public class UserLoraModelServiceImpl extends ServiceImpl<UserLoraModelMapper, UserLoraModelEntity> implements UserLoraModelService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserLoraModelEntity> page = this.page(
                new Query<UserLoraModelEntity>().getPage(params),
                new QueryWrapper<UserLoraModelEntity>()
        );

        return new PageUtils(page);
    }

    public UserLoraModelEntity getUserLoraModelInUse(String userId){
        UserLoraModelEntity result = baseMapper.getInUse(userId);
        return result;
    }

    public List<String> getUserLoraModelHistoryUse(String userId){
        return baseMapper.getHistoryUse(userId);
    }

    public void updateUserLoraModel(String userId, String url){
        baseMapper.changeULM(userId,url);
    }

    public void delUserLoraModelHistory(String userId){
        baseMapper.delHistory(userId);
    }

}
