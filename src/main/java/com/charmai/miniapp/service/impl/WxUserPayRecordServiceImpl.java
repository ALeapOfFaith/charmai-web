package com.charmai.miniapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charmai.miniapp.entity.WxUserPayRecordEntity;
import com.charmai.miniapp.mapper.WxUserPayRecordMapper;
import com.charmai.miniapp.service.WxUserPayRecordService;
import org.springframework.stereotype.Service;

@Service("wxUserPayRecordService")
public class WxUserPayRecordServiceImpl extends ServiceImpl<WxUserPayRecordMapper, WxUserPayRecordEntity> implements WxUserPayRecordService {
}
