package com.charmai.miniapp.scheduler;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.charmai.miniapp.entity.PhotoGenerateTaskEntity;
import com.charmai.miniapp.entity.TaskEntity;
import com.charmai.miniapp.entity.UserLoraModelEntity;
import com.charmai.miniapp.queue.TaskQueue;
import com.charmai.miniapp.service.*;
import com.charmai.miniapp.service.impl.Base64MultipartFile;
import com.charmai.miniapp.utils.ConstantUtils;
import com.charmai.oss.client.service.UploadPicService;
import com.charmai.weixin.entity.WxUser;
import com.charmai.weixin.service.WxUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.charmai.miniapp.service.impl.WxUserInviteCodeServiceImpl.BASE_CHAR_NUMBER;


public class TaskSyncJob implements Runnable {
    private Logger logger = LoggerFactory.getLogger(TaskSyncJob.class);
    private TaskQueue taskQueue;
    private TaskService taskService;
    private UploadHistoryService uploadHistoryService;
    private UserLoraModelService userLoraModelService;
    private UploadPicService uploadPicService;
    private MessageService messageService;
    private WxUserService wxUserService;
    private PhotoGenerateTaskService photoGenerateTaskService;

    public TaskSyncJob(TaskQueue taskQueue, TaskService taskService
            , UploadHistoryService uploadHistoryService
            , UserLoraModelService userLoraModelService
            , UploadPicService uploadPicService
            , MessageService messageService
            , WxUserService wxUserService
            , PhotoGenerateTaskService photoGenerateTaskService) {
        this.taskQueue = taskQueue;
        this.taskService = taskService;
        this.uploadHistoryService = uploadHistoryService;
        this.userLoraModelService = userLoraModelService;
        this.uploadPicService = uploadPicService;
        this.messageService = messageService;
        this.wxUserService = wxUserService;
        this.photoGenerateTaskService = photoGenerateTaskService;
    }

    @Override
    public void run() {
        //同步任务逻辑
        // 获取所有训练中的任务，根据TaskId，gpuAddress轮询任务信息
        String address = "";
        try {
            List<TaskEntity> taskEntityList = taskService.queryUnEndedTask();

            for (TaskEntity taskEntity : taskEntityList) {
                address = taskEntity.getGpuAddress();
                String userId = taskEntity.getUserId();
                TrainInfo trainInfo = SdApi.getTrainStatus(userId, taskEntity.getTaskId(), taskEntity.getGpuAddress());
                // error重新执行
                if (trainInfo != null && !trainInfo.getSuccess() && !trainInfo.getIsTraining()) {
                    // 获取用户图片,base64编码
                    List<File> images = new ArrayList<>();
                    List<String> userUploadUrl = uploadHistoryService.getUploadHistory(userId);
                    if (userUploadUrl.size() > 0) {
                        for (String url : userUploadUrl) {
                            images.add(FileUtils.getPicFile(userId, url));
                        }
//                        // 不足20张，补齐
//                        List<File> copiedImages = new ArrayList<>(images);
//                        for (File object : images) {
//                            copiedImages.add(object);
//                            if (copiedImages.size() >= 20) {
//                                break;
//                            }
//                        }
                        List<File> files = images;
                        if (images.size() > 20) {
                            files = images.subList(0, 20);
                        }
                        // TODO 报错的机器,超过次数，还是放回池子中，记录日志
                        logger.warn("UserId:" + userId + ", Task " + taskEntity.getTaskId() + " running error in " + taskEntity.getGpuAddress());
                        DataLoader.gpuAddressPool.addLoraIp(taskEntity.getGpuAddress());
                        String modelName = userId + "_" + RandomUtil.randomString(BASE_CHAR_NUMBER, 6);
                        taskEntity.setGpuAddress(DataLoader.gpuAddressPool.getLoraIp());
                        String taskId = SdApi.loraTrain(files, modelName, taskEntity.getGpuAddress());
                        if (taskId != null) {
                            // 更新任务ID,机器信息
                            taskService.updateTaskByUserId(userId, taskId, taskEntity.getGpuAddress(), 1);
                            Path directory = Path.of(FileUtils.BaseDir + userId);
                            FileUtils.deleteDirectory(directory);
                        }
                    }

                }
                // 完成的机器重新放入队列
                List<String> sucessLoraList = new ArrayList<>();
                if (trainInfo != null && trainInfo.getSuccess()) {
                    if (trainInfo.getLoraPath() != null || !"".equals(trainInfo.getLoraPath())) {
                        // 后续可能需要lora模型上传云存储
                        File loraFile = new File(trainInfo.getLoraPath());
                        if (loraFile.exists()) {
                            //                    sucessLoraList.add(trainInfo.getLoraPath());
                            String filePath = trainInfo.getLoraPath();
                            String fileSuffix = filePath.substring(filePath.lastIndexOf("."));
                            Path path = Paths.get(filePath);
                            byte[] imageBytes = Files.readAllBytes(path);
                            String base64Image = Base64Encoder.encode(imageBytes);
                            // 解码Base64数据为二进制
                            byte[] data = Base64Utils.decodeFromString(base64Image);
                            MultipartFile multipartFile = new Base64MultipartFile(data, "lora" + fileSuffix, "text/plain");

                            String fileUrl = uploadPicService.uploadToCos(multipartFile, userId);
                            UserLoraModelEntity entity = new UserLoraModelEntity();
                            UserLoraModelEntity userLoraModelInUse = userLoraModelService.getUserLoraModelInUse(userId);
                            if (userLoraModelInUse != null) {
                                entity.setId(userLoraModelInUse.getId());
                            }
                            entity.setUserId(userId);
                            entity.setCreateTime(new Date());
                            entity.setLoraUrl(fileUrl);
                            entity.setLoraName(trainInfo.getLoraName());
                            entity.setDelFlag(0);
                            userLoraModelService.saveOrUpdate(entity);
                            logger.info("TaskSyncJob get user lora success " + trainInfo.getLoraPath() + ", Path: " + trainInfo.getLoraPath());
                            loraFile.delete();
                        }
                        taskService.updateTaskByUserId(userId, taskEntity.getTaskId(), taskEntity.getGpuAddress(), 2);
                        DataLoader.gpuAddressPool.addLoraIp(taskEntity.getGpuAddress());
                        WxUser wxUser = wxUserService.getById(userId);
                        //如果有首次保存的生图任务状态置为初始化
                        QueryWrapper<PhotoGenerateTaskEntity> queryWrapper = new QueryWrapper<>();
                        queryWrapper.orderByAsc("create_time");
                        queryWrapper.lambda().eq(PhotoGenerateTaskEntity::getTaskStatus, ConstantUtils.PHOTO_TASK_STATUS_WAITING_INITIAL_START)
                                .eq(PhotoGenerateTaskEntity::getWxUserId, userId)
                                .eq(PhotoGenerateTaskEntity::getWxUserId, userId)
                                .eq(PhotoGenerateTaskEntity::getType, ConstantUtils.PHOTO_TASK_TYPE_FIRST_TIME);
                        PhotoGenerateTaskEntity photoGenerateTaskEntity = photoGenerateTaskService.getOne(queryWrapper);
                        if (!ObjectUtils.isEmpty(photoGenerateTaskEntity)) {
                            logger.info("start to set  photoGenerateTask:{} initial", photoGenerateTaskEntity.getTaskId());
                            photoGenerateTaskEntity.setTaskStatus(ConstantUtils.PHOTO_TASK_STATUS_INITIAL);
                            photoGenerateTaskService.updateById(photoGenerateTaskEntity);
                        } else {
                            messageService.sendMessage(wxUser.getOpenId());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("TaskSyncJob error : " + e);
        }

    }
}

