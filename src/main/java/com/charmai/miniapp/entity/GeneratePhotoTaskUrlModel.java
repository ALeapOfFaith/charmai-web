package com.charmai.miniapp.entity;

import lombok.Data;

import java.util.List;

@Data
public class GeneratePhotoTaskUrlModel {
    private String taskId;

    private Integer status;

    private String templateName;

    private List<String> urls;
}
