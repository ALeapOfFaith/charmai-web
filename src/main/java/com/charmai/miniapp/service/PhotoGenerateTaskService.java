package com.charmai.miniapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charmai.miniapp.entity.GeneratePhotoTaskUrlModel;
import com.charmai.miniapp.entity.PhotoGenerateTaskEntity;

import java.util.List;

public interface PhotoGenerateTaskService extends IService<PhotoGenerateTaskEntity> {

    List<GeneratePhotoTaskUrlModel> getUserAllGeneratePhotos(String userId);

}
