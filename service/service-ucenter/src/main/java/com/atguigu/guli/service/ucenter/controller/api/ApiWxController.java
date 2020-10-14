package com.atguigu.guli.service.ucenter.controller.api;

import com.atguigu.guli.common.util.HttpClientUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.helper.JwtInfo;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.atguigu.guli.service.ucenter.util.UcenterProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.HashMap;
import java.util.UUID;

@CrossOrigin
@Controller
@RequestMapping("/api/ucenter/wx")
@Slf4j
public class ApiWxController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private UcenterProperties ucenterProperties;

    @GetMapping("login")
    public String genQrConnect(HttpSession session){
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";


        // 处理回调地址
        String redirecturi="";
        try {
            redirecturi = URLEncoder.encode(ucenterProperties.getRedirectUri(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.URL_ENCODE_ERROR);
        }

        // 处理state:生成随机数，存入session
        String state = UUID.randomUUID().toString();
        log.info("生成state = " +state);
        session.setAttribute("wx_open_state",state);

        String qrcodeUrl = String.format(baseUrl,
                ucenterProperties.getAppId(),
                ucenterProperties.getRedirectUri(),
                state);
        return "redirect:"+qrcodeUrl;
    }

    @GetMapping("callback")
    public String callback(String code,String state,HttpSession session){

        // 回调被拉起，并获得code和state参数
        log.info("callback被调用");
        log.info("code = "+code);
        log.info("state = "+state);

        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state)){
            log.error("非法回调请求");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        String sessionState = (String)session.getAttribute("wx_open_state");
        System.err.println(sessionState);
        if (!state.equals(sessionState)){
            log.error("非法回调请求：state参数不一致");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
        HashMap<String, String> accessTokenParam = new HashMap<>();
        accessTokenParam.put("appid",ucenterProperties.getAppId());
        accessTokenParam.put("secret",ucenterProperties.getAppSecret());
        accessTokenParam.put("code",code);
        accessTokenParam.put("grant_type","authorization_code");

        HttpClientUtils client = new HttpClientUtils(accessTokenUrl, accessTokenParam);

        String result = "";


        try{
            // 发送请求
            client.setHttps(true);
            client.get();
            result = client.getContent();
            System.out.println("result = " + result);
        } catch (Exception e){
            log.error("获取access_token失败");
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        Gson gson = new Gson();
        HashMap<String,Object> resultMap = gson.fromJson(result, HashMap.class);

        // 判断微信获取access_token失败的响应
        Object errcodeObj = resultMap.get("errcode");
        if (errcodeObj != null){
            String errmsg = (String)resultMap.get("errmsg");
            Double errcode = (Double)errcodeObj;
            log.error("获取access_token失败 - "+"message: "+errmsg + ",errcode: "+ errcode);
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        // 微信获取access_token响应成功
        String accessToken = (String) resultMap.get("access_token");
        String openid = (String) resultMap.get("openid");

        log.info("accessToken="+accessToken);
        log.info("openid = "+openid);

        // 根据access_token获取微信用户的基本信息
        // 根据openid查询当前用户是否已经使用微信登录过该系统
        Member member = memberService.getByOpenid(openid);
        if (member == null){

            // 向微信的资源服务器发起请求，获取当前用户的用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
            HashMap<String, String> userInfoMap = new HashMap<>();
            userInfoMap.put("access_token",accessToken);
            userInfoMap.put("openid",openid);
            client = new HttpClientUtils(baseUserInfoUrl, userInfoMap);

            String resultUserInfo =null;
            try{
                client.setHttps(true);
                client.get();
                resultUserInfo = client.getContent();
            } catch (Exception e){
                log.error(ExceptionUtils.getStackTrace(e));
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }
            HashMap<String,Object> resultUserInfoMap = gson.fromJson(resultUserInfo, HashMap.class);

            if (resultUserInfoMap.get("errcode")!=null){
                log.error("获取用户信息失败"+",message:"+resultUserInfoMap.get("errmsg"));
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }
            String nickname =(String)resultUserInfoMap.get("nickname");
            String headimgurl = (String) resultUserInfoMap.get("headimgurl");
            Double sex =(Double) resultUserInfoMap.get("sex");

            // 用户注册
            member = new Member();
            member.setOpenid(openid);
            member.setNickname(nickname);
            member.setAvatar(headimgurl);
            member.setSex(sex.intValue());
            memberService.save(member);
        }

        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setId(member.getOpenid());
        jwtInfo.setNickname(member.getNickname());
        jwtInfo.setAvatar(member.getAvatar());
        String token = JwtHelper.createToken(jwtInfo);

        // 携带token跳转
        return "redirect:http://localhost:3000?token="+token;
    }

}
