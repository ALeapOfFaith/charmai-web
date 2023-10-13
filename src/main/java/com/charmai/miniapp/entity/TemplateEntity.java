package com.charmai.miniapp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("model_template")
public class TemplateEntity {
    @TableId
    private String id;
    private String templateId;
    private String templateName;
    private String templateDescription;
    private String templateMapping;
    private String coverUrl;
    private Integer used;



}