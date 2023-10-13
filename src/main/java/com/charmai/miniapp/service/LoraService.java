package com.charmai.miniapp.service;

import com.charmai.miniapp.entity.LoraEntity;
import java.util.List;

public interface LoraService {
    List<LoraEntity>getLoraInfo(String template_id);
}
