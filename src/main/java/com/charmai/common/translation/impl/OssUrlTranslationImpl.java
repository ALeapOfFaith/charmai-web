//package com.charmai.common.translation.impl;
//
//import com.charmai.common.annotation.TranslationType;
//import com.charmai.common.constant.TransConstant;
//import com.charmai.common.core.service.OssService;
//import com.charmai.common.translation.TranslationInterface;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Component;
//
///**
// * OSS翻译实现
// *
// * @author Lion Li
// */
//@Component
//@AllArgsConstructor
//@TranslationType(type = TransConstant.OSS_ID_TO_URL)
//public class OssUrlTranslationImpl implements TranslationInterface<String> {
//
//    private final OssService ossService;
//
//    @Override
//    public String translation(Object key, String other) {
//        return ossService.selectUrlByIds(key.toString());
//    }
//}
