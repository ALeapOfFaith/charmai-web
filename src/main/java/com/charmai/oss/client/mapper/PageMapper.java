package com.charmai.oss.client.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PageMapper {  //mapper用于进行数据库操作

    List<String> getHomePagePic();
}
