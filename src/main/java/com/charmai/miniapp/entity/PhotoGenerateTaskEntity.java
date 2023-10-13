package com.charmai.miniapp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;

import java.util.Date;

@Data
@TableName("photo_generate_task")
public class PhotoGenerateTaskEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Long id;

    private String taskId;

    private String wxUserId;

    private Integer taskStatus;

    private String templateId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 0 用户首次生成
     * 1 用户迭代生成
     */
    private Integer type;
}
