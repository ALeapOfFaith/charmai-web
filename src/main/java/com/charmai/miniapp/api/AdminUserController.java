package com.charmai.miniapp.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.charmai.common.core.domain.R;
import com.charmai.miniapp.entity.AdminUserEntity;
import com.charmai.miniapp.entity.AdminUserVo;
import com.charmai.miniapp.mapper.AdminUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adminuser")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserMapper adminUserMapper;

    @PostMapping("/login")
    public R<AdminUserVo> login(@RequestBody AdminUserEntity adminUserEntity){
        AdminUserEntity resultUser = adminUserMapper.selectByUserName(adminUserEntity.getUserName());
        if (resultUser == null){
            return R.fail("用户不存在");
        }
        String password = resultUser.getPassword();
        if (!password.equals(adminUserEntity.getPassword())){
            return R.fail("用户名或密码错误");
        }
        StpUtil.login("app_user");
        String token = StpUtil.getTokenValue();
        AdminUserVo adminUserVo = AdminUserVo
                .builder()
                .token(token)
                .build();
        BeanUtils.copyProperties(resultUser, adminUserVo);
        return R.ok(adminUserVo);
    }

    @GetMapping("/test")
    public R<String> test(){
        return R.ok("test success!");
    }
}
