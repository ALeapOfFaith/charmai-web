package com.charmai.miniapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charmai.miniapp.entity.UploadHistoryEntity;

import java.util.List;

public interface UploadHistoryService extends IService<UploadHistoryEntity> {
    Boolean saveUpload(String user_id, String photo_url, Double photo_width, Double photo_height);

    List<String> getUploadHistory(String user_id);

    List<UploadHistoryEntity> getUploadHistoryWithHeight(String user_id);
}
