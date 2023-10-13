package com.charmai.miniapp.service.impl;

import com.charmai.miniapp.entity.PhotoGenerateTaskEntity;
import com.charmai.miniapp.service.PhotoGenerateAsyncService;
import com.charmai.miniapp.service.PhotoGenerateTaskService;
import com.charmai.miniapp.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class PhotoGenerateAsyncServiceImpl implements PhotoGenerateAsyncService {

    @Autowired
    TaskService taskService;

    @Autowired
    PhotoGenerateTaskService photoGenerateTaskService;

    @Autowired
    RedissonClient redissonClient;

    @Async("taskExecutor")
    @Override
    public void generatePhoto(String templateId, PhotoGenerateTaskEntity photoGenerateTaskEntity) {
        taskService.generatePhoto(templateId, photoGenerateTaskEntity);
    }


}
