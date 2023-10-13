package com.charmai.miniapp.mapper;

import com.charmai.miniapp.entity.AdminUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminUserMapper {
    @Select("select * from admin_user where user_name = #{userName}")
    AdminUserEntity selectByUserName(String userName);
}
