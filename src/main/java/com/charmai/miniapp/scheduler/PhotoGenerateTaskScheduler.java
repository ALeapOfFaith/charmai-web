package com.charmai.miniapp.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.charmai.miniapp.constant.RedisConstant;
import com.charmai.miniapp.entity.GeneratePhotoSaveEntity;
import com.charmai.miniapp.entity.PhotoGenerateTaskEntity;
import com.charmai.miniapp.service.GeneratePhotoSaveService;
import com.charmai.miniapp.service.MessageService;
import com.charmai.miniapp.service.PhotoGenerateAsyncService;
import com.charmai.miniapp.service.PhotoGenerateTaskService;
import com.charmai.miniapp.utils.ConstantUtils;
import com.charmai.weixin.entity.WxUser;
import com.charmai.weixin.service.WxUserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class PhotoGenerateTaskScheduler {

    @Autowired
    PhotoGenerateAsyncService photoGenerateAsyncService;

    @Autowired
    PhotoGenerateTaskService photoGenerateTaskService;

    @Autowired
    GeneratePhotoSaveService generatePhotoSaveService;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    WxUserService wxUserService;
    @Autowired
    private MessageService messageService;

    @Scheduled(initialDelay = 10000, fixedRate = 60000) //第一次延迟10秒后执行，之后按fixedRate的规则每1分钟执行一次
    public void execute() {
//        log.info("Start image generation timed task");
        QueryWrapper<PhotoGenerateTaskEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("create_time");
        queryWrapper.lambda().eq(PhotoGenerateTaskEntity::getTaskStatus, 1);
        List<PhotoGenerateTaskEntity> photoGenerateTaskEntities = photoGenerateTaskService.list(queryWrapper);

//        log.info("Start image generation timed task，found {} tasks to be generated", photoGenerateTaskEntities.size());
        if (photoGenerateTaskEntities.size() > 0) {
            for (PhotoGenerateTaskEntity photoGenerateTaskEntity : photoGenerateTaskEntities) {
                String address = DataLoader.gpuAddressPool.getIP();
                boolean isFromLoraGpu = false;
                if (address == null) {
                    address = DataLoader.gpuAddressPool.getLoraIp();
                    if (address != null) {
                        isFromLoraGpu = true;
                    }
                } else {
                    DataLoader.gpuAddressPool.addIP(address);
                }
                if (address == null) {
                    log.warn("Gpu is not enough");
                }
                if (address != null && isFromLoraGpu) {
                    DataLoader.gpuAddressPool.addLoraIp(address);
                }
                log.info("Start processing image generation task {}", photoGenerateTaskEntity.getTaskId());
                photoGenerateAsyncService.generatePhoto(photoGenerateTaskEntity.getTemplateId(), photoGenerateTaskEntity);
                //updatePhotoGenerateTaskStatus(photoGenerateTaskEntity, urls);

                log.info("Image generation task {} completed", photoGenerateTaskEntity.getTaskId());
            }
        }
    }

    @Scheduled(initialDelay = 10000, fixedRate = 5000) //第一次延迟10秒后执行，之后按fixedRate的规则每5秒钟执行一次
    public void saveGeneratePhoto() {
//        log.info("Start image generation photo save timed task");
        QueryWrapper<PhotoGenerateTaskEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("create_time");
        queryWrapper.lambda().eq(PhotoGenerateTaskEntity::getTaskStatus, 3);
        List<PhotoGenerateTaskEntity> photoGenerateTaskEntities = photoGenerateTaskService.list(queryWrapper);

//        log.info("Start image generation photo save timed task，found {} tasks to be saved", photoGenerateTaskEntities.size());
        if (photoGenerateTaskEntities.size() > 0) {
            for (PhotoGenerateTaskEntity photoGenerateTaskEntity : photoGenerateTaskEntities) {

                String taskId = photoGenerateTaskEntity.getTaskId();
                String userId = photoGenerateTaskEntity.getWxUserId();
                WxUser wxUser = wxUserService.getById(userId);
                log.info("Start save image generation task {}", taskId);

                RList<String> redissonUrlList = redissonClient.getList(RedisConstant.PHOTO_GENERATE_TASK_URL_LIST + photoGenerateTaskEntity.getTaskId());
                log.info("image generation task {} have {} images need to be save", taskId, redissonUrlList.size());
                if (redissonUrlList.size() > 0) {
                    List<GeneratePhotoSaveEntity> generatePhotoSaveEntities = new ArrayList<>();
                    for (String url : redissonUrlList) {
                        GeneratePhotoSaveEntity generatePhotoSaveEntity = new GeneratePhotoSaveEntity();
                        generatePhotoSaveEntity.setTaskId(taskId);
                        generatePhotoSaveEntity.setPhotoUrl(url);
                        generatePhotoSaveEntity.setCreatTime(new Date());
                        generatePhotoSaveEntity.setUserId(userId);
                        generatePhotoSaveEntity.setDelFlag(1);

                        double width = 0.0;
                        double height = 0.0;
                        try {
                            URL photoUrl = new URL(url);
                            BufferedImage image = ImageIO.read(photoUrl);

                            width = image.getWidth();
                            height = image.getHeight();
                        } catch (IOException e) {
                            log.error("无法读取图片：" + e.getMessage());
                        }
                        generatePhotoSaveEntity.setPhotoHeight(height);
                        generatePhotoSaveEntity.setPhotoWidth(width);

                        generatePhotoSaveEntities.add(generatePhotoSaveEntity);
                    }
                    generatePhotoSaveService.saveBatch(generatePhotoSaveEntities);
                }
                log.info("Image generation task {} save success", taskId);
                photoGenerateTaskEntity.setTaskStatus(4);
                photoGenerateTaskService.saveOrUpdate(photoGenerateTaskEntity);
                //如果是用户首次生成的任务，采用lora模型生成的消息通知
                if (!ObjectUtils.isEmpty(photoGenerateTaskEntity.getType())
                        && ConstantUtils.PHOTO_TASK_TYPE_FIRST_TIME == photoGenerateTaskEntity.getType()) {
                    messageService.sendMessage(wxUser.getOpenId());
                } else {
                    messageService.sendGeneratePhotoMessage(wxUser.getOpenId());
                }
            }
        }
    }

}
