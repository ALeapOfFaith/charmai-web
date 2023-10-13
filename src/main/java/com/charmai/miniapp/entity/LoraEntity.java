package com.charmai.miniapp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

@Data
@TableName("model_lora")
public class LoraEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * LoRA名称
     */
    private String loraName;
    /**
     * LoRA权重
     */
    private Double loraWeight;
    /**
     * 关联模板ID
     */
    private String templateId;

    private String url;
    /**
     * 是否删除（0删除 1不删除）
     */
    private int delFlag;
    /**
     * 创建时间
     */
    private Date creatTime;
}
