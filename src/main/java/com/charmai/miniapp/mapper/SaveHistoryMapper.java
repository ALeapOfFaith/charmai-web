package com.charmai.miniapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charmai.miniapp.entity.AlbumSaveEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SaveHistoryMapper extends BaseMapper<AlbumSaveEntity> {
    Boolean saveSave(String user_id, String photo_url, Double photo_width, Double photo_height);
    List<String> getSaveHistory(String userId);
    List<AlbumSaveEntity> getSaveHistoryWithHeight(String userId);
}
