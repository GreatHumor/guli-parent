package com.atguigu.guli.service.sms.Service.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.sms.util.SmsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/sms/simple")
@RefreshScope
public class ApiSimpleController {

    @Value("${aliyun.sms.signName}")
    private String signName;

    @GetMapping("test1")
    public R test1(){
        return R.ok().data("signName",signName);
    }

    @Autowired
    private SmsProperties smsProperties;

    @GetMapping("test2")
    public R test2(){
        return R.ok().data("smsProperties",smsProperties);
    }

}
