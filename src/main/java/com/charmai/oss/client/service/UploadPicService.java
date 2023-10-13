package com.charmai.oss.client.service;

import com.charmai.miniapp.service.SaveHistoryService;
import com.charmai.miniapp.service.UploadHistoryService;
import com.charmai.oss.client.entity.UserImage;
import com.charmai.oss.client.mapper.UploadPicMapper;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service //相当于UserService加到spring容器里
public class UploadPicService {  //处理业务逻辑

    @Autowired  //相当于实例化UserMapper,自动装配
    private UploadPicMapper uploadPicMapper;
    @Autowired
    private SaveHistoryService saveHistoryService;
    @Autowired
    private UploadHistoryService uploadHistoryService;
    //secretId 秘钥id
    @Value("${spring.tengxun.secretId}")
    private String secretId;
    //SecretKey 秘钥
    @Value("${spring.tengxun.secretKey}")
    private String secretKey;
    // 访问域名
    @Value("${spring.tengxun.url}")
    private String URL;

    @Value("${spring.tengxun.region}")
    private String region;
    // 存储桶名称
    @Value("${spring.tengxun.bucketName}")
    private String bucketName;

    public COSClient initCOSClient(){
//        创建COS 凭证
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region(this.region);
//        配置 COS 区域 就购买时选择的区域 我这里是 南京（nanjing）
        ClientConfig clientConfig = new ClientConfig(region);
//        生成 cos 客户端。
        return new COSClient(cred, clientConfig);
    }


    public String uploadImage(MultipartFile image,String userId,String type) throws IOException {
        String url = uploadToCos(image, userId);

//        UserImage ui=new UserImage(10,null,null,null,URL + "/"+ key);
        if("upload".equals(type)) {
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            uploadHistoryService.saveUpload(userId, url, (double) width, (double) height);
        }
        return url;
    }

    @NotNull
    public String uploadToCos(MultipartFile image, String userId) {
        // MultipartFile代表HTML中form data方式上传的文件
        String fileSuffix = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
        String imageName = userId + "_" + UUID.randomUUID().toString().replace("_", "");
        String key = imageName + fileSuffix;
        File localFile = null;
        try {
            localFile = new File(com.charmai.miniapp.scheduler.FileUtils.BaseDir + key);
            FileUtils.copyInputStreamToFile(image.getInputStream(), localFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        TODO 小图和图像宽高
//        Thumbnails.of(new File("D:/showqrcode.jpg"))
//                .scale(1f) //图片大小（长宽）压缩比例 从0-1，1表示原图
//                .outputQuality(0.5f) //图片质量压缩比例 从0-1，越接近1质量越好
//                .toOutputStream(new FileOutputStream("D:/showqrcode_50.jpg"));


        try {
//            System.out.println("key----------------------" + key);
//            key允许绝大部分UTF-8字符，如果出现”/“其实约等于存储路径
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            COSClient cosClient = initCOSClient();
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            // 设置权限(公开读)
            cosClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            System.out.println("url------------" + URL + "/"+ key);
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
        return URL + "/"+ key;
    }

}
