package com.atguigu.guli.service.trade.controller.api;

import com.atguigu.guli.common.util.HttpClientUtils;
import com.atguigu.guli.common.util.StreamUtils;
import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.service.OrderService;
import com.atguigu.guli.service.trade.service.WebxinPayService;
import com.atguigu.guli.service.trade.utils.WeixinPayProperties;
import com.github.wxpay.sdk.WXPayUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/trade/weixin-pay")
@Api(tags = "网站微信支付")
@Slf4j
public class ApiWeixinPayController {
    @Autowired
    private WebxinPayService webxinPayService;

    @Autowired
    private WeixinPayProperties weixinPayProperties;

    @Autowired
    private OrderService orderService;

    @GetMapping("create-native/{orderNo}")
    public R createNative(@PathVariable String orderNo, HttpServletRequest request){

        System.out.println("orderNo = " + orderNo);
        // 校验登录
        JwtHelper.checkToken(request);

        String remoteAddr = request.getRemoteAddr();
        Map<String, Object> map = webxinPayService.createNative(orderNo, remoteAddr);
        return R.ok().data(map);
    }

    /**
     * 支付回调：注意这里是【post】方式
     */
    @PostMapping("callback/notify")
    public String wxNotify(HttpServletRequest request) throws Exception {
        // 1. 验签
        String notifyXml = StreamUtils.inputStream2String(request.getInputStream(),"utf-8");
        if (WXPayUtil.isSignatureValid(notifyXml,weixinPayProperties.getPartnerKey())){
            System.out.println("验签成功");
            // 2. 解析request
            Map<String, String> notifyXmlMap = WXPayUtil.xmlToMap(notifyXml);
            // 3. 判断连接状态和业务状态，如果状态为SUCCESS
            if ("SUCCESS".equals(notifyXmlMap.get("return_code")) && "SUCCESS".equals(notifyXmlMap.get("result_code"))){
                System.out.println("业务成功");
                //4. 判断请求中的订单金额和商户侧的是否一致
                // 取出商户侧的订单金额
                String orderNo = notifyXmlMap.get("out_trade_no");
                Order order = orderService.getOrderByOrderNo(orderNo);

                if (order != null&& order.getTotalFee() == Long.parseLong(notifyXmlMap.get("total_fee"))){
                    System.out.println("金额校验成功");
                    //5. 业务锁，保证高并发下的幂等性
                    synchronized (this){
                        // 6. 判断支付状态,如果未支付则
                        if (order.getStatus() == 0){
                            System.out.println("修改订单状态");
                            //修改支付状态,记录支付日志，增加课程销量
                            orderService.updateOrderStatus(notifyXmlMap);
                        }
                    }
                    // 返回结果成功
                    HashMap<String, String> returnMap = new HashMap<>();
                    returnMap.put("return_code","SUCCESS");
                    returnMap.put("return_msg","OK");
                    String returnXml = WXPayUtil.mapToXml(returnMap);
                    System.out.println("已经支付，响应结果成功");
                    return returnXml;
                }
            }
        }
        // 返回结果失败
        HashMap<String, String> returnMap = new HashMap<>();
        returnMap.put("return_code","FAIL");
        returnMap.put("return_msg","失败");
        String returnXml = WXPayUtil.mapToXml(returnMap);
        System.out.println("响应结果失败");
        return returnXml;
    }

    @GetMapping("get-sign-key")
    public R getSingKey() throws Exception {
        String signKey = webxinPayService.getSignKey();
        return R.ok().data("signKey", signKey);

    }



























}
