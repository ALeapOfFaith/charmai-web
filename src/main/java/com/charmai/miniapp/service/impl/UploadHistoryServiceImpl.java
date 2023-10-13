package com.charmai.miniapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charmai.miniapp.entity.UploadHistoryEntity;
import com.charmai.miniapp.mapper.UploadHistoryMapper;
import com.charmai.miniapp.service.UploadHistoryService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UploadHistoryServiceImpl
        extends ServiceImpl<UploadHistoryMapper, UploadHistoryEntity> implements UploadHistoryService   {
    final
    UploadHistoryMapper uploadHistoryMapper;

    public UploadHistoryServiceImpl(UploadHistoryMapper uploadHistoryMapper) {
        this.uploadHistoryMapper = uploadHistoryMapper;
    }

    @Override
    public Boolean saveUpload(String user_id, String photo_url, Double photo_width, Double photo_height) {
        return uploadHistoryMapper.saveUpload(user_id, photo_url, photo_width, photo_height, new Date());
    }

    @Override
    public List<String> getUploadHistory(String user_id){
        return uploadHistoryMapper.getUploadHistory(user_id);
    }

    @Override
    public List<UploadHistoryEntity> getUploadHistoryWithHeight(String user_id){
        return uploadHistoryMapper.getUploadHistoryWithHeight(user_id);
    }
}
