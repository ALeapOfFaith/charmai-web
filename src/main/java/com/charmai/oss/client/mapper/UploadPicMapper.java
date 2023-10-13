package com.charmai.oss.client.mapper;

import com.charmai.oss.client.entity.UserImage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UploadPicMapper {  //mapper用于进行数据库操作

    void saveUrl(UserImage ui);
}
