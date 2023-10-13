package com.charmai.miniapp.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.charmai.miniapp.entity.GpuNodeEntity;
import com.charmai.miniapp.entity.PhotoGenerateTaskEntity;
import com.charmai.miniapp.entity.TaskEntity;
import com.charmai.miniapp.queue.GpuAddressPool;
import com.charmai.miniapp.queue.TaskQueue;
import com.charmai.miniapp.service.*;
import com.charmai.oss.client.service.UploadPicService;
import com.charmai.weixin.service.WxUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 应用启动时加载数据
 */
@Slf4j
@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    //private Logger logger = LoggerFactory.getLogger(DataLoader.class);
    public static GpuAddressPool gpuAddressPool;
    public static TaskQueue taskQueue;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UploadHistoryService uploadHistoryService;
    @Autowired
    private UserLoraModelService userLoraModelService;
    @Autowired
    private GpuNodeService gpuNodeService;
    @Autowired
    private UploadPicService uploadPicService;

    @Autowired
    PhotoGenerateTaskService photoGenerateTaskService;
    @Autowired
    MessageService messageService;
    @Autowired
    WxUserService wxUserService;

//    public TaskService getTaskService() {
//        return taskService;
//    }
//
//    public void setTaskService(TaskService taskService) {
//        this.taskService = taskService;
//    }
//
//    public GpuNodeService getGpuNodeService() {
//        return gpuNodeService;
//    }
//
//    public void setGpuNodeService(GpuNodeService gpuNodeService) {
//        this.gpuNodeService = gpuNodeService;
//    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 在此处编写加载数据的逻辑
        log.info("Loading data...");

        //将之前运行中的图片生成任务状态改为1，初始化状态，可以被定时任务重新运行
        initPhotoGenerateRunningTasks();

        // 执行加载未完成任务数据，放入队列中
        taskQueue = new TaskQueue();
        List<TaskEntity> taskEntityList = taskService.queryQueueTask();
        Set<String> set = new HashSet<>();
        if (taskEntityList != null && taskEntityList.size() > 0) {
            for (TaskEntity taskEntity : taskEntityList) {
                if (!set.contains(taskEntity.getUserId())) {
                    set.add(taskEntity.getUserId());
                    taskQueue.enqueue(taskEntity.getUserId());
                }
            }
        }
        // 加载机器信息
        gpuAddressPool = new GpuAddressPool();
        List<GpuNodeEntity> gpuNodeList = gpuNodeService.queryAllAvailable();
        int i = 0;
        if (gpuNodeList != null && gpuNodeList.size() > 0) {
            if (gpuNodeList.size() < 5) {
                for (GpuNodeEntity gpuNodeEntity : gpuNodeList) {
                    gpuAddressPool.addLoraIp(gpuNodeEntity.getAddress());
                }
            } else {
                for (GpuNodeEntity gpuNodeEntity : gpuNodeList) {
                    if (i < 2) {
                        gpuAddressPool.addIP(gpuNodeEntity.getAddress());
                        i++;
                    } else {
                        gpuAddressPool.addLoraIp(gpuNodeEntity.getAddress());
                    }
                }
            }
        }

        ScheduledExecutorService taskSchedledExecutor = Executors.newScheduledThreadPool(5);
        taskSchedledExecutor.scheduleWithFixedDelay(new TaskScheduler(taskQueue, taskService, uploadHistoryService),
                5, 5, TimeUnit.SECONDS);

        ScheduledExecutorService taskSyncSchedledExecutor = Executors.newScheduledThreadPool(1);
        taskSyncSchedledExecutor.scheduleWithFixedDelay(new TaskSyncJob(taskQueue, taskService, uploadHistoryService,
                        userLoraModelService, uploadPicService, messageService, wxUserService, photoGenerateTaskService),
                5, 20, TimeUnit.SECONDS);

        log.info("Data loading completed.");
    }

    public void initPhotoGenerateRunningTasks() {
        log.info("Start init image generation init task");
        QueryWrapper<PhotoGenerateTaskEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("create_time");
        queryWrapper.lambda().eq(PhotoGenerateTaskEntity::getTaskStatus, 2);
        List<PhotoGenerateTaskEntity> photoGenerateTaskEntities = photoGenerateTaskService.list(queryWrapper);

        log.info("Start image generation init task, found {} running tasks need to be init", photoGenerateTaskEntities.size());
        if (photoGenerateTaskEntities.size() > 0) {
            for (PhotoGenerateTaskEntity photoGenerateTaskEntity : photoGenerateTaskEntities) {

                log.info("Start init image generation task {}", photoGenerateTaskEntity.getTaskId());
                photoGenerateTaskEntity.setTaskStatus(1);
                photoGenerateTaskService.saveOrUpdate(photoGenerateTaskEntity);

                log.info("Image generation task {} init completed", photoGenerateTaskEntity.getTaskId());
            }
        }
    }

}

