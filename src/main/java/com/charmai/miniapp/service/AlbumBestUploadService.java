package com.charmai.miniapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charmai.miniapp.entity.AlbumBestUploadEntity;

public interface AlbumBestUploadService extends IService<AlbumBestUploadEntity> {
    AlbumBestUploadEntity getAlbumBestByUserId(String userId);

}
