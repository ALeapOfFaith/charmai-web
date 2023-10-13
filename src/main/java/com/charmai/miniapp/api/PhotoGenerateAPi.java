package com.charmai.miniapp.api;

import com.charmai.common.core.domain.R;
import com.charmai.miniapp.entity.GeneratePhotoTaskUrlModel;
import com.charmai.miniapp.entity.TaskEntity;
import com.charmai.miniapp.scheduler.DataLoader;
import com.charmai.miniapp.service.PhotoGenerateTaskService;
import com.charmai.miniapp.service.SaveHistoryService;
import com.charmai.miniapp.service.TaskService;
import com.charmai.miniapp.service.UserLoraModelService;
import com.charmai.miniapp.vo.PhotoGenerateVo;
import com.charmai.miniapp.vo.QueueVo;
import com.charmai.oss.client.service.UploadPicService;
import com.charmai.weixin.constant.MyReturnCode;
import com.charmai.weixin.utils.ThirdSessionHolder;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.iai.v20180301.IaiClient;
import com.tencentcloudapi.iai.v20180301.models.DetectFaceRequest;
import com.tencentcloudapi.iai.v20180301.models.DetectFaceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.charmai.weixin.constant.MyReturnCode.ERR_90002;

@Slf4j
@RestController
//@AllArgsConstructor
@RequestMapping("/weixin/api/ma/photo")
@Tag(name = "大片生成API")
public class PhotoGenerateAPi {
    @Autowired
    private TaskService taskService;
    @Autowired
    private UploadPicService uploadPicService;
    @Autowired
    private SaveHistoryService saveHistoryService;
    @Autowired
    UserLoraModelService userLoraModelService;

    @Autowired
    PhotoGenerateTaskService photoGenerateTaskService;

    @Value("${spring.tengxun.iai.secretId}")
    private String secretId;
    //SecretKey 秘钥
    @Value("${spring.tengxun.iai.secretKey}")
    private String secretKey;

    private Long getFaceScore(String image) {
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential(secretId, secretKey);
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

            req.setUrl(image);
            req.setNeedFaceAttributes(0L);
            req.setNeedQualityDetection(1L);
            // 返回的resp是一个DetectFaceResponse的实例，与请求对象对应
            DetectFaceResponse resp = client.DetectFace(req);
            log.info("Face Detect resp is : " + resp);
            // 返回score
            if (resp != null && resp.getFaceInfos() != null && resp.getFaceInfos().length > 0
                    && resp.getFaceInfos()[0].getFaceQualityInfo() != null
                    && resp.getFaceInfos()[0].getFaceQualityInfo().getScore() != null) {
                return resp.getFaceInfos()[0].getFaceQualityInfo().getScore();
            }
        } catch (TencentCloudSDKException e) {
            log.error(e.getErrorCode());

        }
        return 0L;
    }

    /**
     * 图片上传
     */
    @PostMapping("/upload")
    @Operation(summary = "图片上传")
    public R upload(@RequestParam("file") MultipartFile image) throws IOException {
        String userId = ThirdSessionHolder.getThirdSession().getWxUserId();
        if (!image.isEmpty()) {
            String uploadUrl = uploadPicService.uploadImage(image, userId, "upload");

            Long faceScore = getFaceScore(uploadUrl);

            if (faceScore != null && faceScore > 70) {
                return R.ok(uploadUrl);
            }
            return R.fail(ERR_90002.getCode(), ERR_90002.getMsg());
        }
        return R.fail("上传图片异常");
    }

    /**
     * 生成大片
     * TODO 可能需要改成 websocket
     */
    @PostMapping("/first/generate")
    @Operation(summary = "保存首次生成大片对应的模板")
    public R firstPhotoGenerate(@RequestBody PhotoGenerateVo photoGenerateVo) {
        String userId = ThirdSessionHolder.getThirdSession().getWxUserId();
        taskService.firstPhotoGenerate(photoGenerateVo.getTemplateId(), userId);
        return R.ok();
    }


    /**
     * 生成数字分身
     */
    @PostMapping("/digitalIdentifier")
    @Operation(summary = "生成数字分身")
    public R<Boolean> digitalIdentifierGenerate(@RequestBody List<String> fileUlrs) {

        // 校验图片数量,小于5张报错，大于5小于20需要复制到20张
//        if (fileUlrs == null || fileUlrs.size() < 15) {
//            return R.fail(MyReturnCode.ERR_90006.getCode(),MyReturnCode.ERR_90006.getMsg());
//        }

        String userId = ThirdSessionHolder.getThirdSession().getWxUserId();
        taskService.digitalIdentifierGenerate(userId);

        return R.ok(true);
    }

    /**
     * 获取用户等待时长
     */
    @GetMapping("/digitalWaitTime")
    @Operation(summary = "获取用户等待时长")
    public R<Integer> getWaitTime() {

        //已经训练好的，返回固定code
        String id = ThirdSessionHolder.getThirdSession().getWxUserId();
        TaskEntity taskEntity = taskService.queryRunningTaskByUserId(id);
        if (taskEntity != null) {
            Date startTime = taskEntity.getStartTime();
//            Date startTime = taskEntity.getCreatTime();
            if (startTime == null) {
                return R.fail(MyReturnCode.ERR_90004.getCode(), MyReturnCode.ERR_90004.getMsg());
            }
            int time = Math.toIntExact(30 - (new Date().getTime() - startTime.getTime()) / 1000 / 60);
            // 暂时先计算60分钟的
            if (time < 0) {
                time = time + 30;
            }
            return R.ok(time);
        }

        if (DataLoader.taskQueue.getPosition(id) != 0) {
            return R.ok(DataLoader.taskQueue.getTime(id));
        }
        return R.ok(30);
    }

    /**
     * 获取用户排队位置
     */
    @GetMapping("/queue")
    @Operation(summary = "获取用户排队位置")
    public R<QueueVo> getQueue() {

        //已经训练好的，返回固定code
        String id = ThirdSessionHolder.getThirdSession().getWxUserId();

        Integer position = DataLoader.taskQueue.getPosition(id);
        Integer total = DataLoader.taskQueue.total();
        if (position != null && total != null) {
            QueueVo queueVo = new QueueVo();
            queueVo.setPosition(position);
            queueVo.setTotal(total);
            return R.ok(queueVo);
        }
        return R.fail("用户任务不存在");
    }

    /**
     * 生成大片
     * TODO 可能需要改成 websocket
     */
    @PostMapping("/generate")
    @Operation(summary = "生成大片")
    public R<String> photoGenerate(@RequestBody PhotoGenerateVo photoGenerateVo) {
        String userId = ThirdSessionHolder.getThirdSession().getWxUserId();
        String taskId = taskService.generatePhotoTask(photoGenerateVo.getTemplateId(), userId);
        return R.ok(taskId);
    }

    @GetMapping("/getPhoto")
    @Operation(summary = "获取生成的大片")
    public R<List> getGeneratePhoto(@RequestParam(value = "taskId", required = true) String taskId) {
        String userId = ThirdSessionHolder.getThirdSession().getWxUserId();
        log.info("user " + userId + " start get generate photo task " + taskId + "photos");
        List<String> fileUrls = taskService.getGeneratePhotos(taskId, userId);
        if (fileUrls == null) {
            return R.fail(MyReturnCode.ERR_90005.getCode(), MyReturnCode.ERR_90005.getMsg());
        }
        return R.ok(fileUrls);
    }

    @GetMapping("/getAllPhotos")
    @Operation(summary = "获取用户生成大片任务及对应的大片")
    public R<List> getUserAllGeneratePhotos() {
        String userId = ThirdSessionHolder.getThirdSession().getWxUserId();
        log.info("user " + userId + " start get all photo task and photos");
        List<GeneratePhotoTaskUrlModel> res = photoGenerateTaskService.getUserAllGeneratePhotos(userId);
        if (res == null) {
            return R.fail(MyReturnCode.ERR_90007.getCode(), MyReturnCode.ERR_90007.getMsg());
        }
        return R.ok(res);
    }

    /**
     * 保存图片
     */
    @PostMapping("/save")
    @Operation(summary = "保存图片")
    public R<Boolean> save(@RequestBody List<String> fileUrls) {
        String id = ThirdSessionHolder.getThirdSession().getWxUserId();

        return R.ok(saveHistoryService.saveSave(id, fileUrls));
    }

    /**
     * 保存图片
     */
    @GetMapping("/getRunningTask")
    @Operation(summary = "查询用户是否有运行中的人像模型训练任务")
    public R<TaskEntity> getRunningTask() {
        String userId = ThirdSessionHolder.getThirdSession().getWxUserId();
        TaskEntity taskEntityRunning = taskService.queryRunningTaskByUserId(userId);
        if (taskEntityRunning != null) {
            return R.ok(taskEntityRunning);
        } else {
            return R.ok();
        }
    }

    /**
     * 获取排队任务
     */
    @GetMapping("/queue/task")
    @Operation(summary = "获取用户排队位置")
    public R<CopyOnWriteArrayList> getQueueTask() {

        //已经训练好的，返回固定code
        String id = ThirdSessionHolder.getThirdSession().getWxUserId();

        CopyOnWriteArrayList<String> queue = DataLoader.taskQueue.getQueue();

        return R.ok(queue);
    }

}
