package com.atguigu.guli.service.trade.service.impl;

import com.atguigu.guli.common.util.HttpClientUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.service.OrderService;
import com.atguigu.guli.service.trade.service.WebxinPayService;
import com.atguigu.guli.service.trade.utils.WeixinPayProperties;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WebxinPayServiceImpl implements WebxinPayService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WeixinPayProperties weixinPayProperties;

    @Override
    public Map<String, Object> createNative(String orderNo, String remoteAddr) {
        HttpClientUtils client = null;
        HashMap<String, String> params = null;
        Order order = null;
        try {
            //client = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/unifiedorder");// 正式接口
            client = new HttpClientUtils("https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder");//仿真接口
            params = new HashMap<>();
            params.put("appid", weixinPayProperties.getAppId());
            params.put("mch_id", weixinPayProperties.getPartner());
            params.put("nonce_str", WXPayUtil.generateNonceStr());
            // 获取商品订单
            order = orderService.getOrderByOrderNo(orderNo);
            params.put("body", order.getCourseTitle());
            params.put("out_trade_no", orderNo);

            //注意，这里必须使用字符串类型的参数（总金额：分）
            String totalFee = order.getTotalFee() + "";
            params.put("total_fee", totalFee);

            params.put("spbill_create_ip", remoteAddr);
            params.put("notify_url", weixinPayProperties.getNotifyUrl());
            params.put("trade_type", "NATIVE");

//            // 生成签名
//            String signature = WXPayUtil.generateSignature(params, weixinPayProperties.getPartnerKey());
//            params.put("sign", signature);
            //将参数转换成xml字符串格式：生成带有签名的xml格式字符串
            String xmlParams = WXPayUtil.generateSignedXml(params, weixinPayProperties.getPartnerKey());
            log.info("\n xmlParams：\n" + xmlParams);

            client.setXmlParam(xmlParams);
            client.setHttps(true);

            // 发送请求
            client.post();

            // 获取响应结果
            String resultXml = client.getContent();
            // 解析结果
            Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);
            //错误处理
            if ("FAIL".equals(resultMap.get("return_code")) || "FAIL".equals(resultMap.get("result_code"))) {
                log.error("微信支付统一下单错误 - "
                        + "return_code: " + resultMap.get("return_code")
                        + "return_msg: " + resultMap.get("return_msg")
                        + "result_code: " + resultMap.get("result_code")
                        + "err_code: " + resultMap.get("err_code")
                        + "err_code_des: " + resultMap.get("err_code_des"));

                throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
            }

            // 组装返回所需数据
            HashMap<String, Object> map = new HashMap<>();
            map.put("result_code", resultMap.get("result_code"));//响应码
            map.put("code_url", resultMap.get("code_url"));//生成二维码的url
            map.put("course_id", order.getCourseId());//课程id
            map.put("total_fee", order.getTotalFee());//订单总金额
            map.put("out_trade_no", orderNo);//订单号

            return map;

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }

    }

    @Override
    public String getSignKey() {
        try {
            String url = "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey";
            HttpClientUtils client = new HttpClientUtils(url);
            Map<String, String> params = new HashMap<>();
            params.put("mch_id", weixinPayProperties.getPartner());
            params.put("nonce_str", WXPayUtil.generateNonceStr());
            String xmlParams = WXPayUtil.generateSignedXml(params, weixinPayProperties.getPartnerKey());
            client.setXmlParam(xmlParams);
            client.setHttps(true);

            //发送请求
            client.post();

            //得到响应
            String content = client.getContent();
            System.out.println("content = " + content);

            //解析响应结果
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);
            return resultMap.get("sandbox_signkey");
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw  new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }
    }
}
