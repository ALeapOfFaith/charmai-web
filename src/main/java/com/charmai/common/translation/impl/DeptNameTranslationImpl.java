//package com.charmai.common.translation.impl;
//
//import com.charmai.common.annotation.TranslationType;
//import com.charmai.common.constant.TransConstant;
//import com.charmai.common.core.service.DeptService;
//import com.charmai.common.translation.TranslationInterface;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Component;
//
///**
// * 部门翻译实现
// *
// * @author Lion Li
// */
//@Component
//@AllArgsConstructor
//@TranslationType(type = TransConstant.DEPT_ID_TO_NAME)
//public class DeptNameTranslationImpl implements TranslationInterface<String> {
//
//    private final DeptService deptService;
//
//    @Override
//    public String translation(Object key, String other) {
//        return deptService.selectDeptNameByIds(key.toString());
//    }
//}
