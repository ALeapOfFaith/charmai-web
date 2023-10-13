package com.charmai.miniapp.api;


import com.charmai.common.core.domain.R;
import com.charmai.miniapp.service.MessageService;
import com.charmai.weixin.utils.ThirdSessionHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 成大事
 * @since 2022/7/27 23:11
 */
@Slf4j
@RestController
@RequestMapping("/wx/msg")
@AllArgsConstructor
public class WxMessageController {

    private final MessageService messageService;


    @PostMapping("/sendMessage")
    public R sendMsg() {
        String id = ThirdSessionHolder.getThirdSession().getOpenId();
        return messageService.sendMessage(id);
    }
}
