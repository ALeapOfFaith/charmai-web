package com.charmai.miniapp.mapper;

import com.charmai.miniapp.entity.UserLoraModelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 数字分身
 *
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-08-03 23:56:44
 */
@Mapper
public interface UserLoraModelMapper extends BaseMapper<UserLoraModelEntity> {
    void addULM(UserLoraModelEntity userLoraModelEntity);

    UserLoraModelEntity getInUse(String userId);

    List<String> getHistoryUse(String userId);

    void changeULM(String userId, String url);

    void delHistory(String userId);
}
