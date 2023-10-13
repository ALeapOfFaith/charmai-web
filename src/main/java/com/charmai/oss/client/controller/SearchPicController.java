//package com.charmai.oss.client.controller;
//
//import com.charmai.oss.client.service.SearchPicService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/weixin/api/ma/album")
//public class SearchPicController {
//
//    @Autowired
//    private SearchPicService searchPicService;
//
//    @PostMapping("/uploadPics")
//    public String searchPic(@RequestParam("userId")String userId) throws IOException {
//        String s = searchPicService.searchPic(userId);
//        if(s!="")
//            return "查询到的图片地址为:" + s;
//        else
//            return "查询到的图片记录为0";
//
//    }
//}
