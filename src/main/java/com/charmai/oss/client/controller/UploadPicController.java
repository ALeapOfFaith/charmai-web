//package com.charmai.oss.client.controller;
//
//import com.charmai.oss.client.service.UploadPicService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/weixin/api/ma/photo")
//public class UploadPicController {
//
//    @Autowired
//    private UploadPicService uploadPicService;
//
//    @PostMapping("/upload")
////    @ResponseBody    据说@RestController=@Controller+@ResponseBody
//    public String uploadPic(MultipartFile image,@RequestParam("user_id")String userId) throws IOException{
//        if (image == null) {
//            return "文件为空";
//        }
//        //在增加一步，检查传过来文件的后缀，判断它是不是图片？
////        return uploadPicService.uploadImage(image,userId);
//        return "上传成功，访问地址为:"+uploadPicService.uploadImage(image,userId);
//    }
//
//}
