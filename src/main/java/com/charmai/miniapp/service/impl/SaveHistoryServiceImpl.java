package com.charmai.miniapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charmai.miniapp.entity.AlbumSaveEntity;
import com.charmai.miniapp.mapper.SaveHistoryMapper;
import com.charmai.miniapp.service.SaveHistoryService;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
public class SaveHistoryServiceImpl
        extends ServiceImpl<SaveHistoryMapper, AlbumSaveEntity> implements SaveHistoryService {
    final
    SaveHistoryMapper saveHistoryMapper;

    public SaveHistoryServiceImpl(SaveHistoryMapper saveHistoryMapper) {
        this.saveHistoryMapper = saveHistoryMapper;
    }

    @Override
    public Boolean saveSave(String user_id, List<String> photoUrls) {
        if (photoUrls != null && photoUrls.size() > 0) {
            for (String photoUrl : photoUrls) {
                double width = 0.0;
                double height = 0.0;
                try {
                    URL url = new URL(photoUrl);
                    BufferedImage image = ImageIO.read(url);

                    width = image.getWidth();
                    height = image.getHeight();
                } catch (IOException e) {
                    log.error("无法读取图片：" + e.getMessage());
                }
                saveHistoryMapper.saveSave(user_id, photoUrl,width,height);
            }
        }
        return true;
    }
    @Override
    public List<String> getSaveHistory(String user_id){
        return saveHistoryMapper.getSaveHistory(user_id);
    }
    @Override
    public List<AlbumSaveEntity> getSaveHistoryWithHeight(String user_id){
        return saveHistoryMapper.getSaveHistoryWithHeight(user_id);
    }
}
