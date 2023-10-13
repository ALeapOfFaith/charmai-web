package com.charmai.miniapp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("album_best_upload")
public class AlbumBestUploadEntity  implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 图片ID
     */
    @TableId
    private Long photoId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 图片URL
     */
    private String photoUrl;
    /**
     * 图片宽度
     */
    private Double photoWidth;
    /**
     * 图片高度
     */
    private Double photoHeight;
    /**
     * 是否删除（0删除 1不删除）
     */
    private Integer delFlag;
    /**
     * 创建时间
     */
    private Date creatTime;

}
