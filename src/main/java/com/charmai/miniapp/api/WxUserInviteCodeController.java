package com.charmai.miniapp.api;

import com.charmai.common.core.domain.R;
import com.charmai.miniapp.service.WxUserInviteCodeService;
import com.charmai.miniapp.vo.WxUserInviteCodeResponseVo;
import com.charmai.weixin.utils.ThirdSessionHolder;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Xie
 * @Date: 2023-07-28-22:59
 * @Description: 邀请码接口Api
 */
@RestController
@RequestMapping("/weixin/api/invite")
public class WxUserInviteCodeController {

    @Autowired
    WxUserInviteCodeService wxUserInviteCodeService;


    @GetMapping("/code")
    @Operation(summary = "生成邀请码")
    public R<WxUserInviteCodeResponseVo> getInvitationCode() {
        String user_id = ThirdSessionHolder.getThirdSession().getWxUserId();
        WxUserInviteCodeResponseVo invitationCode = wxUserInviteCodeService.getInviteCode(user_id);
        return R.ok(invitationCode);
    }

}
