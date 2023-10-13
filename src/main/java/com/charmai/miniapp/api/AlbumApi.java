package com.charmai.miniapp.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.charmai.common.core.domain.R;
import com.charmai.miniapp.entity.AlbumBestUploadEntity;
import com.charmai.miniapp.entity.AlbumSaveEntity;
import com.charmai.miniapp.entity.UploadHistoryEntity;
import com.charmai.miniapp.service.AlbumBestUploadService;
import com.charmai.miniapp.service.SaveHistoryService;
import com.charmai.miniapp.service.UploadHistoryService;
import com.charmai.weixin.utils.ThirdSessionHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/weixin/api/ma/album")
@Tag(name = "相册API")
public class AlbumApi {
    /**
     * 查询用户上传图片
     */
    UploadHistoryService uploadHistoryService;
    @GetMapping("/userUploadPics")
    @Operation(summary = "查询用户上传图片")
    public R<List<UploadHistoryEntity>> getUploadPics() {
        String userId = ThirdSessionHolder.getThirdSession().getWxUserId();
        QueryWrapper<UploadHistoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("creat_time");
        queryWrapper.lambda().eq(UploadHistoryEntity::getDelFlag, 1);
        queryWrapper.lambda().eq(UploadHistoryEntity::getUserId, userId);
        List<UploadHistoryEntity> fileUrls = uploadHistoryService.list(queryWrapper);
        return R.ok(fileUrls);
    }

    @PostMapping("/deleteUserUploadPics")
    @Operation(summary = "删除用户上传图片")
    public R deleteUploadPics() {
        String userId = ThirdSessionHolder.getThirdSession().getWxUserId();
        List<UploadHistoryEntity> fileUrls = uploadHistoryService.getUploadHistoryWithHeight(userId);
        if (fileUrls != null && fileUrls.size() > 0) {
            for (UploadHistoryEntity uploadHistoryEntity : fileUrls) {
                uploadHistoryEntity.setDelFlag(0);
            }
            uploadHistoryService.saveOrUpdateBatch(fileUrls);
        }
        return R.ok("删除上传图片成功");
    }

    /**
     * 查询保存的图片
     */
    SaveHistoryService saveHistoryService;
    @GetMapping("/userSavePics")
    @Operation(summary = "查询保存的图片")
    public R<List<AlbumSaveEntity>> getSavePics() {
        String userId = ThirdSessionHolder.getThirdSession().getWxUserId();
        List<AlbumSaveEntity> fileUrls = saveHistoryService.getSaveHistoryWithHeight(userId);
        return R.ok(fileUrls);
    }

    AlbumBestUploadService albumBestUploadService;
    /**
     * 保存最好的图片
     */
    @GetMapping("/saveBestPics")
    @Operation(summary = "保存最好的图片")
    public R SaveBestPics(@RequestParam(value = "photoUrl", required = true) String photoUrl) {
        String userId = ThirdSessionHolder.getThirdSession().getWxUserId();
        AlbumBestUploadEntity albumBestUploadEntity = new AlbumBestUploadEntity();
        albumBestUploadEntity.setCreatTime(new Date());
        albumBestUploadEntity.setUserId(userId);
        albumBestUploadEntity.setPhotoUrl(photoUrl);
        double width = 0.0;
        double height = 0.0;
        try {
            URL url = new URL(photoUrl);
            BufferedImage image = ImageIO.read(url);

            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException e) {
            log.error("无法读取图片：" + e.getMessage());
            return R.fail("读取图片宽高失败");
        }
        albumBestUploadEntity.setDelFlag(1);
        albumBestUploadEntity.setPhotoWidth(width);
        albumBestUploadEntity.setPhotoHeight(height);
        albumBestUploadService.save(albumBestUploadEntity);
        return R.ok();
    }
}
