package com.charmai.miniapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charmai.miniapp.entity.UserLoraModelEntity;
import com.charmai.miniapp.service.impl.PageUtils;

import java.util.Map;

/**
 * 数字分身
 *
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-08-03 23:56:44
 */
public interface UserLoraModelService extends IService<UserLoraModelEntity> {

    PageUtils queryPage(Map<String, Object> params);

    UserLoraModelEntity getUserLoraModelInUse(String userId);
    void delUserLoraModelHistory(String userId);
}
