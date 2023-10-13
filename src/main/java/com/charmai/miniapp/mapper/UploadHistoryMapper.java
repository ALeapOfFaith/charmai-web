package com.charmai.miniapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charmai.miniapp.entity.UploadHistoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Mapper
public interface UploadHistoryMapper extends BaseMapper<UploadHistoryEntity> {
    Boolean saveUpload(String user_id, String photo_url, Double photo_width, Double photo_height, Date date);

    List<String> getUploadHistory(String user_id);
    List<UploadHistoryEntity> getUploadHistoryWithHeight(String user_id);
}
