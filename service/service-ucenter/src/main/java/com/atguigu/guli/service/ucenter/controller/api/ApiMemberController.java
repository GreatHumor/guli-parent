package com.atguigu.guli.service.ucenter.controller.api;

import com.atguigu.guli.common.util.FormUtils;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.ucenter.entity.form.RegisterForm;
import com.atguigu.guli.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(tags = "会员管理")
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/api/ucenter/member")
public class ApiMemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R register(
            @ApiParam(value = "注册表单",required = true)
            @RequestBody RegisterForm registerForm){

        String nickname = registerForm.getNickname();
        String mobile = registerForm.getMobile();
        String password = registerForm.getPassword();
        String code = registerForm.getCode();

        // 校验参数
        if (StringUtils.isEmpty(mobile)
            || !FormUtils.isMobile(mobile)
            || StringUtils.isEmpty(password)
            || StringUtils.isEmpty(code)
            || StringUtils.isEmpty(nickname)){
            return R.setResult(ResultCodeEnum.PARAM_ERROR);
        }

        // 检验验证码
        String key = "checkCode::"+mobile;
        String checkCode = (String)redisTemplate.opsForValue().get(key);
        if (!code.equals(checkCode)){// 注意把code放在前面，否则可能会报空指针异常
           return R.setResult(ResultCodeEnum.CODE_ERROR);
        }

        memberService.register(registerForm);
        return R.ok().message("注册成功");
    }

}
