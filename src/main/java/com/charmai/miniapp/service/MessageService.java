package com.charmai.miniapp.service;

import com.charmai.common.core.domain.R;

/**
 * @author 成大事
 * @since 2022/7/27 23:11
 */
public interface MessageService {
    /**
     * 发送模板消息
     * @return
     */
    R sendMessage(String openId);

    R sendGeneratePhotoMessage(String openId);

}
