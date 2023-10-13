package com.charmai.oss.client.service;

import com.charmai.oss.client.entity.UserImage;
import  com.charmai.oss.client.mapper.SearchPicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@Service
public class SearchPicService {

    @Autowired  //相当于实例化UserMapper,自动装配
    private SearchPicMapper searchPicMapper;

    public String searchPic(String userId) throws IOException {
        List<UserImage> ui=searchPicMapper.searchPic(userId);
        StringBuffer sb=new StringBuffer(128);
        ui.forEach(e -> sb.append(e.getUrl() + "\n"));
        return sb.toString();
    }
}
