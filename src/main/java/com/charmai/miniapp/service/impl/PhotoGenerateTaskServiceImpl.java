package com.charmai.miniapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charmai.miniapp.entity.GeneratePhotoSaveEntity;
import com.charmai.miniapp.entity.GeneratePhotoTaskUrlModel;
import com.charmai.miniapp.entity.PhotoGenerateTaskEntity;
import com.charmai.miniapp.entity.TemplateEntity;
import com.charmai.miniapp.mapper.PhotoGenerateTaskMapper;
import com.charmai.miniapp.service.GeneratePhotoSaveService;
import com.charmai.miniapp.service.PhotoGenerateTaskService;
import com.charmai.miniapp.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("photoGenerateTaskService")
public class PhotoGenerateTaskServiceImpl extends ServiceImpl<PhotoGenerateTaskMapper, PhotoGenerateTaskEntity> implements PhotoGenerateTaskService {
    @Autowired
    TemplateService templateService;
    @Autowired
    GeneratePhotoSaveService generatePhotoSaveService;
    @Override
    public List<GeneratePhotoTaskUrlModel> getUserAllGeneratePhotos(String userId) {
        QueryWrapper<PhotoGenerateTaskEntity> photoGenerateTaskEntityQueryWrapper = new QueryWrapper<>();
        photoGenerateTaskEntityQueryWrapper.lambda().eq(PhotoGenerateTaskEntity::getWxUserId, userId);
        photoGenerateTaskEntityQueryWrapper.lambda().in(PhotoGenerateTaskEntity::getTaskStatus, 1, 2, 3, 4);
        List<PhotoGenerateTaskEntity> tasks = baseMapper.selectList(photoGenerateTaskEntityQueryWrapper);
        List<GeneratePhotoTaskUrlModel> generatePhotoTaskUrlModelList = new ArrayList<>();
        if (tasks.size() > 0) {
            for (PhotoGenerateTaskEntity task : tasks) {
                TemplateEntity template = templateService.getTemplateById(task.getTemplateId());
                GeneratePhotoTaskUrlModel generatePhotoTaskUrlModel = new GeneratePhotoTaskUrlModel();
                generatePhotoTaskUrlModel.setTaskId(task.getTaskId());
                generatePhotoTaskUrlModel.setStatus(task.getTaskStatus());
                if (template != null) {
                    generatePhotoTaskUrlModel.setTemplateName(template.getTemplateName());
                }
                String taskId = task.getTaskId();
                QueryWrapper<GeneratePhotoSaveEntity> generatePhotoTaskUrlModelQueryWrapper = new QueryWrapper<>();
                generatePhotoTaskUrlModelQueryWrapper.lambda().eq(GeneratePhotoSaveEntity::getTaskId, taskId);
                generatePhotoTaskUrlModelQueryWrapper.lambda().eq(GeneratePhotoSaveEntity::getDelFlag, 1);
                List<GeneratePhotoSaveEntity> generatePhotoSaveEntities = generatePhotoSaveService.list(generatePhotoTaskUrlModelQueryWrapper);
                List<String> urls = generatePhotoSaveEntities.stream().map(GeneratePhotoSaveEntity::getPhotoUrl).collect(Collectors.toList());
                generatePhotoTaskUrlModel.setUrls(urls);
                generatePhotoTaskUrlModelList.add(generatePhotoTaskUrlModel);
            }
        }
        return generatePhotoTaskUrlModelList;
    }
}
