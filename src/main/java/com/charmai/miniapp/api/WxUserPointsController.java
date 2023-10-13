package com.charmai.miniapp.api;


import com.charmai.common.core.domain.R;
import com.charmai.miniapp.service.WxUserPointsService;
import com.charmai.miniapp.vo.IncreasePointsRequestVo;
import com.charmai.miniapp.vo.WxUserPointsResponseVo;
import com.charmai.weixin.entity.WxOpenDataDTO;
import com.charmai.weixin.utils.ThirdSessionHolder;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 
 *
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-07-30 17:55:47
 */
@RestController
@RequestMapping("/weixin/api/user")
public class WxUserPointsController {
    @Autowired
    private WxUserPointsService wxUserPointsService;


    @GetMapping("/points")
    @Operation(summary = "查询用户积分")
    public R<WxUserPointsResponseVo> getWxUserTotalPoints() {
        String user_id = ThirdSessionHolder.getThirdSession().getWxUserId();
        WxUserPointsResponseVo wxUserPointsResponseVo = wxUserPointsService.getWxUserTotalPoints(user_id);
        return R.ok(wxUserPointsResponseVo);
    }

    @PostMapping("/operate/increase/points")
    @Operation(summary = "运营增加用户积分")
    public R increasePoints(@RequestBody IncreasePointsRequestVo increasePointsRequest) {

         wxUserPointsService.operateIncreasePoints(increasePointsRequest);
        return R.ok();
    }


}
