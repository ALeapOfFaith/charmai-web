package com.charmai.oss.client.service;

import com.charmai.oss.client.mapper.PageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageService {

    @Autowired  //相当于实例化UserMapper,自动装配
    private PageMapper pageMapper;

    public List<String> getHomePagePic(){
        return pageMapper.getHomePagePic();
    }
}
