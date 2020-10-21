package com.atguigu.guli.service.trade.service;

import java.util.Map;

public interface WebxinPayService {
    Map<String,Object> createNative(String orderNo,String remoteAddr);

    String getSignKey();
}
