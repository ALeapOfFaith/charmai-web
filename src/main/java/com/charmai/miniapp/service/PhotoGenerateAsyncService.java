package com.charmai.miniapp.service;

import com.charmai.miniapp.entity.PhotoGenerateTaskEntity;

import java.util.List;

public interface PhotoGenerateAsyncService {
    void generatePhoto(String templateId, PhotoGenerateTaskEntity photoGenerateTaskEntity);
}
