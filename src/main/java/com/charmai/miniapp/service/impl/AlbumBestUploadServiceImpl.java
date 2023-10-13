package com.charmai.miniapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charmai.miniapp.entity.AlbumBestUploadEntity;
import com.charmai.miniapp.entity.TaskEntity;
import com.charmai.miniapp.mapper.AlbumBestUploadMapper;
import com.charmai.miniapp.service.AlbumBestUploadService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("AlbumBestUploadService")
public class AlbumBestUploadServiceImpl extends ServiceImpl<AlbumBestUploadMapper, AlbumBestUploadEntity> implements AlbumBestUploadService {


    @Override
    public AlbumBestUploadEntity getAlbumBestByUserId(String userId) {
        List<AlbumBestUploadEntity> albumBestUploadEntityList = baseMapper.selectList(new QueryWrapper<AlbumBestUploadEntity>().eq("user_id", userId));
        if (albumBestUploadEntityList != null && albumBestUploadEntityList.size() > 0) {
            return albumBestUploadEntityList.get(0);
        }
        return null;
    }
}
