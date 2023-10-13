package com.charmai.miniapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charmai.miniapp.entity.AlbumSaveEntity;

import java.util.List;

public interface SaveHistoryService extends IService<AlbumSaveEntity> {

    Boolean saveSave(String userId, List<String> photoUrl);

    List<String> getSaveHistory(String userId);
    List<AlbumSaveEntity> getSaveHistoryWithHeight(String userId);
}
