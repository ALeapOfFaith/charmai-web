package com.charmai.miniapp.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.charmai.miniapp.entity.ModelLoraEntity;
import com.charmai.miniapp.service.impl.PageUtils;

import java.util.Map;

/**
 * 
 *
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-08-06 15:10:27
 */
public interface ModelLoraService extends IService<ModelLoraEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

