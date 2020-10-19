package com.atguigu.guli.service.trade.controller.api;


import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-10-16
 */
@RestController
@RequestMapping("/api/trade/order")
@Api(tags = "网站订单管理")
@Slf4j
public class ApiOrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("新增订单")
    @PostMapping("auth/save/{courseId}")
    public R save(@PathVariable String courseId, HttpServletRequest request){

        String memberId = JwtHelper.getId(request);
        String orderId = orderService.saveOrder(courseId,memberId);
        return R.ok().data("orderId",orderId);

    }

    @ApiOperation("获取订单")
    @GetMapping("auth/get/{orderId}")
    public R get(@PathVariable String orderId,HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        Order order = orderService.getByOrderId(orderId,memberId);
        return R.ok().data("item",order);
    }

    @ApiOperation("判断课程是否购买")
    @GetMapping("auth/is-buy/{orderId}")
    public R isBuyByCourseId(String courseId,HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        Boolean isBuy = orderService.isBuyByCourseId(courseId, memberId);
        return R.ok().data("isBuy", isBuy);
    }

    @ApiOperation("获取订单列表")
    @GetMapping("auth/list")
    public R list(HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        List<Order> orderList = orderService.selectByMemberId(memberId);
        return R.ok().data("items",orderList);
    }

    @ApiOperation("删除订单")
    @DeleteMapping("auth/remove/{orderId}")
    public R removeByOrderId(@PathVariable String orderId,HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        boolean result = orderService.removeByIdAndMember(orderId,memberId);
        if (result){
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据订单号查询订单状态")
    @GetMapping("query-pay-status/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
        boolean result = orderService.queryPayStatus(orderNo);
        if (result){
            return R.ok().message("支付成功");
        }
        return R.setResult(ResultCodeEnum.PAY_RUN);
    }

}

