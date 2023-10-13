package com.charmai.miniapp.scheduler;

import cn.hutool.core.util.RandomUtil;
import com.charmai.miniapp.queue.TaskQueue;
import com.charmai.miniapp.service.TaskService;
import com.charmai.miniapp.service.UploadHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.charmai.miniapp.service.impl.WxUserInviteCodeServiceImpl.BASE_CHAR_NUMBER;


public class TaskScheduler implements Runnable {
    private Logger logger = LoggerFactory.getLogger(TaskScheduler.class);
    private TaskQueue taskQueue;
    private TaskService taskService;
    private UploadHistoryService uploadHistoryService;

    public TaskScheduler(TaskQueue taskQueue, TaskService taskService, UploadHistoryService uploadHistoryService) {
        this.taskQueue = taskQueue;
        this.taskService = taskService;
        this.uploadHistoryService = uploadHistoryService;
    }

    @Override
    public void run() {
        String userId = taskQueue.dequeue();
        String gpuAddress = "";
        boolean isFromLoraGpu = false;
        try {
            // 派发任务逻辑
            // 获取队首任务

            if (userId == null) {
                return;
            }
            logger.info("Start Processing userId: " + userId);
            gpuAddress = DataLoader.gpuAddressPool.getLoraIp();
//            if (gpuAddress == null) {
//                gpuAddress = DataLoader.gpuAddressPool.getIP();
                if (gpuAddress == null) {
                    logger.warn("Gpu is not enough");
                    DataLoader.taskQueue.toTop(userId);
                    return;
                }
//                isFromLoraGpu = true;
//            }
            // 调用HTTP接口下发任务
            // 获取用户图片,base64编码
            List<File> images = new ArrayList<>();
            List<String> userUploadUrl = uploadHistoryService.getUploadHistory(userId);
            if (userUploadUrl.size() > 0) {
                for (String url : userUploadUrl) {
                    images.add(FileUtils.getPicFile(userId, url));
                }
                // 不足20张，补齐
//                List<File> copiedImages = new ArrayList<>(images);
//                for (File object : images) {
//                    copiedImages.add(object);
//                    if (copiedImages.size() >= 20) {
//                        break;
//                    }
//                }
                List<File> files = images;
                if (images.size() > 20) {
                    files = images.subList(0, 20);
                }
                // 调用数字分身接口

                String modelName = userId + "_" + RandomUtil.randomString(BASE_CHAR_NUMBER, 6);
                String taskId = SdApi.loraTrain(files, modelName, gpuAddress);
                if (taskId != null) {
                    // 保存任务ID,机器信息
                    Path directory = Path.of(FileUtils.BaseDir + userId);
                    FileUtils.deleteDirectory(directory);
                    taskService.updateTaskByUserId(userId, taskId, gpuAddress, 0);
                } else {
                    if(isFromLoraGpu){
                        DataLoader.gpuAddressPool.addLoraIp(gpuAddress);
                    }else {
                        DataLoader.gpuAddressPool.addIP(gpuAddress);
                    }
                    DataLoader.taskQueue.toTop(userId);
                }
            }
        } catch (Exception e) {
            logger.error("TaskScheduler error : " + userId +" "+e);
            DataLoader.taskQueue.toTop(userId);
            if (!"".equals(gpuAddress)) {
                if(isFromLoraGpu){
                    DataLoader.gpuAddressPool.addLoraIp(gpuAddress);
                }else {
                    DataLoader.gpuAddressPool.addIP(gpuAddress);
                }
            }
        }
    }
}

