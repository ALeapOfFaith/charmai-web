package com.charmai.miniapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charmai.miniapp.entity.GeneratePhotoSaveEntity;
import com.charmai.miniapp.mapper.GeneratePhotoSaveMapper;
import com.charmai.miniapp.service.GeneratePhotoSaveService;
import org.springframework.stereotype.Service;

@Service("GeneratePhotoSaveService")
public class GeneratePhotoSaveServiceImpl extends ServiceImpl<GeneratePhotoSaveMapper, GeneratePhotoSaveEntity> implements GeneratePhotoSaveService {

}
