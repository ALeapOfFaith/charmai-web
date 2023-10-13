package com.charmai.miniapp.scheduler;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.iai.v20180301.IaiClient;
import com.tencentcloudapi.iai.v20180301.models.*;

import static com.charmai.miniapp.scheduler.SdApiTest.convertToBase64;

public class DetectFace
{
    public static void main(String [] args) {
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential("AKIDujQ3d5PdfXEg6Ul3MYuBMQL4Z0Mi0Uqp", "sbEjBVvHahcQ7c1LYm6LPK6nVFzfGpHf");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("iai.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            IaiClient client = new IaiClient(cred, "", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DetectFaceRequest req = new DetectFaceRequest();
            String folderPath = "C:\\Users\\Administrator\\Downloads\\微信图片_20230805151702.png"; // 文件夹路径
            String base64Image1 = convertToBase64(folderPath);
//            req.setImage(base64Image1);
            req.setUrl("https://test001-1311934233.cos.ap-nanjing.myqcloud.com/1689264255225061377_6c467589-54b8-4aab-8077-ba8ee460504e.jpg");
            req.setNeedFaceAttributes(0L);
            req.setNeedQualityDetection(1L);
            // 返回的resp是一个DetectFaceResponse的实例，与请求对象对应
            DetectFaceResponse resp = client.DetectFace(req);
            // 输出json格式的字符串回包
            System.out.println(DetectFaceResponse.toJsonString(resp.getFaceInfos()[0].getFaceQualityInfo()));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }
}
