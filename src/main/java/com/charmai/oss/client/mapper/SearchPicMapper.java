package com.charmai.oss.client.mapper;

import com.charmai.oss.client.entity.UserImage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchPicMapper {  //mapper用于进行数据库操作

    List<UserImage> searchPic(String userId);
}
