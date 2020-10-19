package com.atguigu.guli.service.trade.service;

import com.atguigu.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author Helen
 * @since 2020-10-16
 */
public interface OrderService extends IService<Order> {

    String saveOrder(String courseId, String memberId);

    Order getByOrderId(String orderId, String memberId);

    Boolean isBuyByCourseId(String courseId, String memberId);

    Order getOrderByOrderNo(String orderNo);

    List<Order> selectByMemberId(String memberId);

    boolean removeByIdAndMember(String orderId, String memberId);

    void updateOrderStatus(Map<String, String> notifyXmlMap);

    boolean queryPayStatus(String orderNo);
}
