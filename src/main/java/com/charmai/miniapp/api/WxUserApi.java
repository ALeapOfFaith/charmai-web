/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 */
package com.charmai.miniapp.api;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import com.charmai.common.core.domain.R;
import com.charmai.miniapp.entity.UserLoraModelEntity;
import com.charmai.miniapp.service.MyWxPayService;
import com.charmai.miniapp.service.UserLoraModelService;
import com.charmai.miniapp.service.WxUserInviteCodeService;
import com.charmai.miniapp.vo.UserLoraModelVo;
import com.charmai.weixin.config.WxMaConfiguration;
import com.charmai.weixin.entity.LoginMaDTO;
import com.charmai.weixin.entity.WxOpenDataDTO;
import com.charmai.weixin.entity.WxUser;
import com.charmai.weixin.service.WxUserService;
import com.charmai.weixin.utils.ThirdSessionHolder;
import com.charmai.weixin.utils.WxMaUtil;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.charmai.weixin.constant.MyReturnCode.ERR_90001;

/**
 * 微信用户
 *
 * @author www.joolun.com
 * @date 2019-08-25 15:39:39
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/weixin/api/ma/wxuser")
@Tag(name = "小程序用户API")
public class WxUserApi {

    private final WxUserService wxUserService;

    @Autowired
    WxUserInviteCodeService wxUserInviteCodeService;

    @Autowired
    MyWxPayService myWxPayService;

    /**
     * 小程序用户登录
     *
     * @param request
     * @param loginMaDTO
     * @return
     */
    @Operation(summary = "小程序用户登录")
    @PostMapping("/login")
    public R login(HttpServletRequest request, @RequestBody LoginMaDTO loginMaDTO) {
        try {
            WxUser wxUser = wxUserService.loginMa(WxMaUtil.getAppId(request), loginMaDTO);
            return R.ok(wxUser);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    /**
     * 获取用户信息
     *
     * @param
     * @return
     */
    @Operation(summary = "获取用户信息")
    @GetMapping
    public R get() {
        String id = ThirdSessionHolder.getThirdSession().getWxUserId();
        return R.ok(wxUserService.getById(id));
    }

    /**
     * 保存用户信息
     *
     * @param wxOpenDataDTO
     * @return
     */
    @Operation(summary = "保存用户信息")
    @PostMapping
    public R saveOrUpdateWxUser(@RequestBody WxOpenDataDTO wxOpenDataDTO) {
        wxOpenDataDTO.setAppId(ThirdSessionHolder.getThirdSession().getAppId());
        wxOpenDataDTO.setUserId(ThirdSessionHolder.getThirdSession().getWxUserId());
        wxOpenDataDTO.setSessionKey(ThirdSessionHolder.getThirdSession().getSessionKey());
        WxUser wxUser = wxUserService.saveOrUptateWxUser(wxOpenDataDTO);
        return R.ok(wxUser);
    }

    @Operation(summary = "生成二维码")
    @Parameters({
            @Parameter(name = "envVersion", description = "默认release ，要打开的小程序版本。正式版为 release，体验版为trial，开发版为develop")})
    @GetMapping(value = "/createQrCode")
    public R<String> createQrCode(@RequestParam(value = "envVersion", required = false) String envVersion,
                                  HttpServletRequest request, HttpServletResponse response) throws IOException {

        //调用工具包的服务
        String appId = WxMaUtil.getAppId(request);
        WxMaService wxMaService = WxMaConfiguration.getMaService(appId);

        String user_id = ThirdSessionHolder.getThirdSession().getWxUserId();
        String inviteCode = wxUserInviteCodeService.getInviteCode(user_id).getCode();

        String envVersionStr = "release";
        if (envVersion != null && !envVersion.isEmpty()) {
            envVersionStr = envVersion;
        }

        // 设置小程序二维码线条颜色为黑色
        WxMaCodeLineColor lineColor = new WxMaCodeLineColor("0", "0", "0");
        byte[] qrCodeBytes = null;
        try {
            qrCodeBytes = wxMaService.getQrcodeService().createWxaCodeUnlimitBytes(inviteCode, "pages/index/index", false, envVersionStr, 430, true, lineColor, false);
        } catch (WxErrorException e) {
            log.error("createQrCode error : " + e.getMessage());
        }
        //String qrCodeStr = Base64.encodeBase64String(qrCodeBytes);
        // 设置contentType
//        response.setContentType("image/png");
//        if (ObjectUtils.isEmpty(qrCodeBytes)) {
//            // 读取图片文件
//            ClassPathResource imageResource = new ClassPathResource("测试二维码图.png");
//            InputStream inputStream = imageResource.getInputStream();
//            // 将图片数据读取
//            qrCodeBytes = inputStream.readAllBytes();
//        }


        // 写入response的输出流中
//        OutputStream stream = response.getOutputStream();
//        stream.write(qrCodeBytes);
//        stream.flush();
//        stream.close();

        return R.ok("", Base64.encodeBase64String(qrCodeBytes));
    }

    @Operation(summary = "校验用户是否完成支付（安卓校验支付订单，ios校验积分充值）")
    @GetMapping(value = "/check/complete/payment")
    public R<Boolean> checkCompletePayment() {
        String id = ThirdSessionHolder.getThirdSession().getWxUserId();
        return R.ok(wxUserService.checkCompletePayment(id));
    }

    @Autowired
    UserLoraModelService userLoraModelService;

    /**
     * 获取用户生成的lora模型
     *
     * @return
     */
    @GetMapping(value = "getUserLoraModel")
    public R<UserLoraModelVo> getUserLoraModel() {
        String userId = ThirdSessionHolder.getThirdSession().getWxUserId();
        UserLoraModelEntity userLoraModelInUse = userLoraModelService.getUserLoraModelInUse(userId);
        if (ObjectUtils.isEmpty(userLoraModelInUse)) {
            return R.fail(ERR_90001.getCode(), ERR_90001.getMsg());
        }
        UserLoraModelVo userLoraModelVo = new UserLoraModelVo();
        userLoraModelVo.setLoraName(userLoraModelInUse.getLoraName());
        userLoraModelVo.setCreateTime(userLoraModelInUse.getCreateTime());
        userLoraModelVo.setUserId(userLoraModelInUse.getUserId());
        return R.ok(userLoraModelVo);
    }
}
