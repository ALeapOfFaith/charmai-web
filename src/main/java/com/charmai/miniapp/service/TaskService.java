package com.charmai.miniapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charmai.miniapp.entity.PhotoGenerateTaskEntity;
import com.charmai.miniapp.entity.TaskEntity;
import com.charmai.miniapp.service.impl.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 */
public interface TaskService extends IService<TaskEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<TaskEntity> queryQueueTask();

    List<TaskEntity> queryUnEndedTask();

    TaskEntity queryTaskByUserId(String userId);
    TaskEntity queryRunningTaskByUserId(String userId);

    Integer updateTaskByUserId(String userId, String taskId, String gpuAddress, Integer status);
    List<String> generatePhoto(String templateId, PhotoGenerateTaskEntity photoGenerateTaskEntity);

    String generatePhotoTask(String templateId, String userId);

    List<String> getGeneratePhotos(String taskId, String userId);

    void digitalIdentifierGenerate(String userId);

    void firstPhotoGenerate(String templateId, String userId);
}

