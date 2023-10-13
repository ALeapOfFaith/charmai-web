package com.charmai.miniapp.service.impl;

import cn.hutool.core.codec.Base64Encoder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charmai.common.core.domain.R;
import com.charmai.common.exception.ServiceException;
import com.charmai.miniapp.constant.ExceptionConstant;
import com.charmai.miniapp.constant.RedisConstant;
import com.charmai.miniapp.entity.*;
import com.charmai.miniapp.mapper.TaskMapper;
import com.charmai.miniapp.mapper.WxUserPointsMapper;
import com.charmai.miniapp.scheduler.DataLoader;
import com.charmai.miniapp.scheduler.FileUtils;
import com.charmai.miniapp.scheduler.ModelExist;
import com.charmai.miniapp.scheduler.SdApi;
import com.charmai.miniapp.service.*;
import com.charmai.miniapp.utils.ConstantUtils;
import com.charmai.miniapp.utils.RedisClientUtils;
import com.charmai.oss.client.service.UploadPicService;
import com.charmai.weixin.constant.MyReturnCode;
import com.charmai.weixin.utils.ThirdSessionHolder;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("taskService")
public class TaskServiceImpl extends ServiceImpl<TaskMapper, TaskEntity> implements TaskService {
    private Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    LoraService loraService;
    @Autowired
    TemplateService templateService;
    @Autowired
    UploadPicService uploadPicService;

    @Autowired
    PhotoGenerateTaskService photoGenerateTaskService;

    @Autowired
    WxUserPointsService wxUserPointsService;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    UserLoraModelService userLoraModelService;
    @Autowired
    WxUserPointsMapper wxUserPointsMapper;

    @Autowired
    AlbumBestUploadService albumBestUploadService;
    @Autowired
    private UploadHistoryService uploadHistoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TaskEntity> page = this.page(
                new Query<TaskEntity>().getPage(params),
                new QueryWrapper<TaskEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public TaskEntity queryTaskByUserId(String userId) {
        List<TaskEntity> taskEntityList = baseMapper.selectList(new QueryWrapper<TaskEntity>().eq("user_id", userId).eq("status", 0));
        if (taskEntityList != null && taskEntityList.size() > 0) {
            return taskEntityList.get(0);
        }
        return null;
    }

    @Override
    public TaskEntity queryRunningTaskByUserId(String userId) {
        List<TaskEntity> taskEntityList = baseMapper.selectList(new QueryWrapper<TaskEntity>().eq("user_id", userId).eq("status", 1));
        if (taskEntityList != null && taskEntityList.size() > 0) {
            return taskEntityList.get(0);
        }
        return null;
    }

    @Override
    public Integer updateTaskByUserId(String userId, String taskId, String gpuAddress, Integer status) {
        TaskEntity taskEntity;
        if (status == 0) {
            taskEntity = this.queryTaskByUserId(userId);
            taskEntity.setTaskId(taskId);
            taskEntity.setStartTime(new Date());
            taskEntity.setGpuAddress(gpuAddress);
            // 0的时候，需要将创建好的任务状态改为运行中
            taskEntity.setStatus(1);
            return baseMapper.updateById(taskEntity);
        }
        // 运行中的任务更新状态
        if (status == 1) {
            taskEntity = this.queryRunningTaskByUserId(userId);
            taskEntity.setTaskId(taskId);
            taskEntity.setStartTime(new Date());
            taskEntity.setGpuAddress(gpuAddress);
            taskEntity.setStatus(status);
            return baseMapper.updateById(taskEntity);
        }
        if (status == 2) {
            taskEntity = this.queryRunningTaskByUserId(userId);
            taskEntity.setTaskId(taskId);
            taskEntity.setEndTime(new Date());
            taskEntity.setStatus(status);
            return baseMapper.updateById(taskEntity);
        }
        return -1;
    }

    @Override
    public List<TaskEntity> queryUnEndedTask() {
        return baseMapper.selectList(new QueryWrapper<TaskEntity>().eq("status", 1));
    }

    @Override
    public List<TaskEntity> queryQueueTask() {
        return baseMapper.selectList(new QueryWrapper<TaskEntity>().eq("status", 0));
    }

    @Override
    public List<String> generatePhoto(String templateId, PhotoGenerateTaskEntity photoGenerateTaskEntity) {
        String userId = photoGenerateTaskEntity.getWxUserId();
        UserLoraModelEntity userLoraModelInUse = userLoraModelService.getUserLoraModelInUse(userId);
        if (userLoraModelInUse == null) {
            return null;
        }

        // 根据用户选择的lora，组成promt参数，可能需要从选择的lora中获取参数模版，然后填充信息组起来
        List<LoraEntity> loraInfo = loraService.getLoraInfo(templateId);
        TemplateEntity template = templateService.getTemplateById(templateId);
        // 调用底下生图接口, 从gpu池中获取一台gpu机器，如果没有就从loraIp中获取，如果没有就一直获取，设置超时时间
        String address = DataLoader.gpuAddressPool.getIP();
        boolean isFromLoraGpu = false;
        if (address == null) {
            address = DataLoader.gpuAddressPool.getLoraIp();
            if (address != null) {
                isFromLoraGpu = true;
            }
        }
        if (address == null) {
            // TODO设置时间循环等待
            logger.warn("Gpu is not enough");
            return null;
        }
        photoGenerateTaskEntity.setTaskStatus(2);
        photoGenerateTaskEntity.setStartTime(new Date());
        photoGenerateTaskService.saveOrUpdate(photoGenerateTaskEntity);
        List<String> fileUrls = new ArrayList<>();

        // 姿势的图，先从resource position目录下获取固定4张 controlnetimages
        String imagePath = "controlnetimages/r6.png";
        List<File> controlNetImages = new ArrayList<>();
//        controlNetImages.add(new File(imagePath));
        String templateName = "default";

        if (template != null && loraInfo != null && loraInfo.size() > 0) {
            imagePath = "controlnetimages/" + loraInfo.get(0).getLoraName();
            List<File> loraFolderFiles = FileUtils.getControlNetImage(imagePath);
            if (loraFolderFiles != null && loraFolderFiles.size() > 0) {
                for (File loraFolderFile : loraFolderFiles) {
                    if (loraFolderFile.getName().endsWith(".txt")) {
                        continue;
                    }
                    controlNetImages.add(loraFolderFile);
                }
            }
            templateName = template.getTemplateMapping();

        }
        for (int i = 0; i < 2; i++) {
            StringBuilder prompt = new StringBuilder();
            StringBuilder negative_prompt = new StringBuilder();
            File image = pickRandomFile(controlNetImages);
            String imageAbsolutePath = image.getAbsolutePath();
            String promptPath = imageAbsolutePath.substring(0, imageAbsolutePath.lastIndexOf(".")) + ".txt";
            File jsonFile = new File(promptPath);
            if (jsonFile.exists()) {
                String jsonString = FileUtils.getJsonString(promptPath);
                JSONObject jsonObject = JSON.parseObject(jsonString);
                prompt.append(jsonObject.get("positive"));
                negative_prompt.append(jsonObject.get("negative"));
            }
            // 传人脸照片
            File faceFile = new File("/tmp/face/" + userId + ".png");
            if (!faceFile.exists()) {
                AlbumBestUploadEntity albumBestUploadEntity = albumBestUploadService.getAlbumBestByUserId(userId);
                if (albumBestUploadEntity != null) {
                    String photoUrl = albumBestUploadEntity.getPhotoUrl();
                    if (photoUrl != null) {
                        try {
                            URL url = new URL(photoUrl);
                            BufferedImage faceImage = ImageIO.read(url);
//                        将 BufferedImage 写入到文件
                            ImageIO.write(faceImage, "png", faceFile);
                            logger.info("Downolad face pic " + faceFile.getAbsolutePath() + " success!");
                        } catch (IOException e) {
                            log.error("无法读取图片：" + e);
                        }
                    }
                } else {
                    logger.info("have no  face pic!");
                }
            }

            if (loraInfo != null) {
                for (LoraEntity loraEntity : loraInfo) {
                    prompt.append("<lora:").append(loraEntity.getLoraName()).append(":").append(loraEntity.getLoraWeight()).append(">,");
                }
            }
            String userLora = "<lora:" + userLoraModelInUse.getLoraName() + ":" + 0.7 + ">";
            prompt.append(userLora);

            List<ModelExist> modelList = SdApi.getModelList("lora", address);
            Set<String> setLora = new HashSet<>();
            String modelName = userId + ".safetensors";
            if (modelList != null && modelList.size() > 0) {
                for (ModelExist modelExist : modelList) {
                    setLora.add(modelExist.getModel_name());
                }
                if (loraInfo != null) {
                    for (LoraEntity loraEntity : loraInfo) {
                        if (loraEntity.getUrl() != null && !setLora.contains(loraEntity.getUrl())) {
                            SdApi.uploadModel(userId, loraEntity.getUrl(), "lora", loraEntity.getLoraName(), address);
                        }
                    }
                }
                modelName = userLoraModelInUse.getLoraName() + ".safetensors";
                if (!setLora.contains(modelName)) {
                    SdApi.uploadModel(userId, userLoraModelInUse.getLoraUrl(), "lora", modelName, address);
                }
            } else {
                if (loraInfo != null) {
                    for (LoraEntity loraEntity : loraInfo) {
                        if (loraEntity.getUrl() != null) {
                            SdApi.uploadModel(userId, loraEntity.getUrl(), "lora", loraEntity.getLoraName(), address);
                        }
                    }
                }
                SdApi.uploadModel(userId, userLoraModelInUse.getLoraUrl(), "lora", modelName, address);
            }
            List<String> list = SdApi.generatePhoto(userId, templateName, prompt.toString(), negative_prompt.toString(), image, faceFile, address);

            // 获取返回的图片，上传云存储，返回云存储url
            if (list != null) {
                int k = 0;
                for (String s : list) {
                    if (k == 2) {
                        break;
                    }
                    fileUrls.add(s);
                    k++;
                }
            } else {
                photoGenerateTaskEntity.setTaskStatus(1);
                photoGenerateTaskEntity.setStartTime(new Date());
                photoGenerateTaskService.saveOrUpdate(photoGenerateTaskEntity);
            }
            if (isFromLoraGpu) {
                DataLoader.gpuAddressPool.addLoraIp(address);
            } else {
                DataLoader.gpuAddressPool.addIP(address);
            }
        }
        List<String> generateImageUrls = new ArrayList<>();
        if (fileUrls.size() > 0) {
            for (String fileUrl : fileUrls) {
                // 解码Base64数据为二进制
                byte[] imageData = Base64Utils.decodeFromString(fileUrl);
                MultipartFile multipartFile = new Base64MultipartFile(imageData, "image.png", "image/png");
                String url = uploadPicService.uploadToCos(multipartFile, userId);
                generateImageUrls.add(url);
            }

            updatePhotoGenerateTaskStatus(photoGenerateTaskEntity, generateImageUrls);

        }
        return generateImageUrls;
    }

    public void updatePhotoGenerateTaskStatus(PhotoGenerateTaskEntity photoGenerateTaskEntity, List<String> urls) {
        String lockName = RedisConstant.PHOTO_GENERATE_TASK_LOCK + photoGenerateTaskEntity.getTaskId();
        long waitTime = 200;

        if (!RedisClientUtils.tryLock(lockName, waitTime, TimeUnit.MILLISECONDS)) {
            RedisClientUtils.unLock(lockName);
            log.error("generate Photo Task repeat submit exception");
            throw new ServiceException(ExceptionConstant.PHOTO_GENERATE_TASK_REPEAT_SUBMIT_EXCEPTION_MSG,
                    ExceptionConstant.PHOTO_GENERATE_TASK_REPEAT_SUBMIT_EXCEPTION_CODE);
        }

        try {
            photoGenerateTaskEntity.setTaskStatus(3);
            RList<String> redissonUrlList = redissonClient.getList(RedisConstant.PHOTO_GENERATE_TASK_URL_LIST + photoGenerateTaskEntity.getTaskId());
            redissonUrlList.addAll(urls);
            photoGenerateTaskEntity.setEndTime(new Date());
            photoGenerateTaskService.saveOrUpdate(photoGenerateTaskEntity);
            logger.info("Image generation task " + photoGenerateTaskEntity.getTaskId() + " results have been cached in Redis");
        } finally {
            RedisClientUtils.unLock(lockName);
        }
    }

    private static File pickRandomFile(List<File> list) {
        Random random = new Random();
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }

    @Override
    public String generatePhotoTask(String templateId, String userId) {
        WxUserPointsEntity wxUserPoints = wxUserPointsMapper.selectOne(new QueryWrapper<WxUserPointsEntity>().eq("wx_user_id", userId));

        if (ObjectUtils.isEmpty(wxUserPoints)
                || wxUserPoints.getTotalPoints() - 50 < 0) {
            throw new ServiceException(ExceptionConstant.USER_POINTS_NOT_ENOUGH_SUBMIT_EXCEPTION_MSG,
                    ExceptionConstant.USER_POINTS_NOT_ENOUGH_EXCEPTION_CODE);
        }

        String taskId = UUID.randomUUID().toString().replaceAll("-", "");
        PhotoGenerateTaskEntity photoGenerateTaskEntity = new PhotoGenerateTaskEntity();
        photoGenerateTaskEntity.setTaskId(taskId);
        photoGenerateTaskEntity.setWxUserId(userId);
        photoGenerateTaskEntity.setTemplateId(templateId);
        photoGenerateTaskEntity.setTaskStatus(1);
        photoGenerateTaskEntity.setType(ConstantUtils.PHOTO_TASK_TYPE_ITERATION);
        photoGenerateTaskEntity.setCreateTime(new Date());
        photoGenerateTaskService.save(photoGenerateTaskEntity);

        //创建任务成功后，扣除50积分
        wxUserPointsService.deductWxUserPoints(userId, 50);
        logger.info("user " + userId + " generate photo task " + taskId + " create success!");
        return taskId;
    }

    @Override
    public List<String> getGeneratePhotos(String taskId, String userId) {
        QueryWrapper<PhotoGenerateTaskEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PhotoGenerateTaskEntity::getTaskId, taskId)
                .eq(PhotoGenerateTaskEntity::getWxUserId, userId);
        PhotoGenerateTaskEntity photoGenerateTaskEntity = photoGenerateTaskService.getOne(queryWrapper);

        if (photoGenerateTaskEntity.getTaskStatus() == 3) {

            logger.info("user " + userId + " generate photo task " + taskId + " query success!");
            List<String> fileUrls = new ArrayList<>();
            RList<String> redissonUrlList = redissonClient.getList(RedisConstant.PHOTO_GENERATE_TASK_URL_LIST + photoGenerateTaskEntity.getTaskId());
            fileUrls.addAll(redissonUrlList);
            return fileUrls;

//            if (fileUrls != null && !fileUrls.isEmpty()){
//                return fileUrls;
//            }else {
//                updatePhotoGenerateTaskStatus(photoGenerateTaskEntity);
//            }
        }
        logger.info("user query " + userId + " generate photo task " + taskId + "incomplete!");
        return null;
    }

    public void digitalIdentifierGenerate(String userId) {
        // 查询用户上传的图片，并下载图片
//        String userId = ThirdSessionHolder.getThirdSession().getWxUserId();
        List<String> userUploadUrl = uploadHistoryService.getUploadHistory(userId);
        if (userUploadUrl.size() <= 0) {
            return;
        }
        TaskEntity taskEntity = this.queryTaskByUserId(userId);
        if (taskEntity != null) {
            R.fail(MyReturnCode.ERR_90003.getCode(), MyReturnCode.ERR_90003.getMsg());
        }
        TaskEntity taskEntityRunning = this.queryRunningTaskByUserId(userId);
        if (taskEntityRunning != null) {
            R.fail(MyReturnCode.ERR_90004.getCode(), MyReturnCode.ERR_90004.getMsg());
        }
        //已经训练过的，先删除原来的人像lora模型
        userLoraModelService.delUserLoraModelHistory(userId);

        // 保存任务信息，任务加入队列，返回任务ID
        TaskEntity task = new TaskEntity();
        task.setUserId(userId);
        task.setCreatTime(new Date());
        task.setStatus(0);
        this.save(task);

        DataLoader.taskQueue.enqueue(userId);
    }

    @Override
    public void firstPhotoGenerate(String templateId, String userId) {
        /**
         * 查询用户是否存在默认的生成图片任务（taskStatus 为0的任务）
         *  1.存在更换模板id
         *  2.不存在保存生图任务，对应状态为0，类型为首次生图任务
         */
        QueryWrapper<PhotoGenerateTaskEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("create_time");
        queryWrapper.lambda().eq(PhotoGenerateTaskEntity::getTaskStatus, ConstantUtils.PHOTO_TASK_STATUS_WAITING_INITIAL_START)
                .eq(PhotoGenerateTaskEntity::getWxUserId, userId)
                .eq(PhotoGenerateTaskEntity::getType, ConstantUtils.PHOTO_TASK_TYPE_FIRST_TIME);
        PhotoGenerateTaskEntity photoGenerateTaskEntity = photoGenerateTaskService.getOne(queryWrapper);
        if (!ObjectUtils.isEmpty(photoGenerateTaskEntity)) {
            photoGenerateTaskEntity.setTemplateId(templateId);
            photoGenerateTaskService.updateById(photoGenerateTaskEntity);
        } else {
            String taskId = UUID.randomUUID().toString().replaceAll("-", "");
            photoGenerateTaskEntity = new PhotoGenerateTaskEntity();
            photoGenerateTaskEntity.setTaskId(taskId);
            photoGenerateTaskEntity.setWxUserId(userId);
            photoGenerateTaskEntity.setTemplateId(templateId);
            photoGenerateTaskEntity.setTaskStatus(ConstantUtils.PHOTO_TASK_STATUS_WAITING_INITIAL_START);
            photoGenerateTaskEntity.setType(ConstantUtils.PHOTO_TASK_TYPE_FIRST_TIME);
            photoGenerateTaskEntity.setCreateTime(new Date());
            photoGenerateTaskService.save(photoGenerateTaskEntity);
        }

    }

    private static String convertToBase64(String filePath) {
        String base64Image = "";
        try {
            Path path = Paths.get(filePath);
            byte[] imageBytes = Files.readAllBytes(path);
            base64Image = Base64Encoder.encode(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64Image;
    }
}