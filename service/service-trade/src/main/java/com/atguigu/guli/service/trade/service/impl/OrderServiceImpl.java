package com.atguigu.guli.service.trade.service.impl;

import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.entity.PayLog;
import com.atguigu.guli.service.trade.feign.EduCourseService;
import com.atguigu.guli.service.trade.feign.UcenterMemberService;
import com.atguigu.guli.service.trade.mapper.OrderMapper;
import com.atguigu.guli.service.trade.mapper.PayLogMapper;
import com.atguigu.guli.service.trade.service.OrderService;
import com.atguigu.guli.service.trade.utils.OrderNoUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-10-16
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private UcenterMemberService ucenterMemberService;

    @Autowired
    private PayLogMapper payLogMapper;

    @Override
    public String saveOrder(String courseId, String memberId) {

        // 查询当前用户是否已有当前课程的订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("member_id",memberId);
        Order orderExist = baseMapper.selectOne(queryWrapper);
        if (orderExist != null){
            return orderExist.getId();//如果订单已存在，则直接返回订单id
        }

        // 查询课程信息
        System.out.println(courseId);
        R rCourse = eduCourseService.getCourseDtoById(courseId);
        System.out.println(rCourse);
        ObjectMapper mapper = new ObjectMapper();
        CourseDto courseDto = mapper.convertValue(rCourse.getData().get("courseDto"), CourseDto.class);
        System.out.println(courseDto);
        if (courseDto == null){
            throw new GuliException(ResultCodeEnum.REMOTE_CALL_ERROR);
        }

        // 查询用户信息
        System.out.println(memberId);
        R rMember = ucenterMemberService.getMemberDtoById(memberId);
        System.out.println(rMember);
        MemberDto memberDto = mapper.convertValue(rMember.getData().get("memberDto"), MemberDto.class);
        if(memberDto == null){
            throw new GuliException(ResultCodeEnum.REMOTE_CALL_ERROR);
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNo(OrderNoUtils.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseDto.getTitle());
        order.setCourseCover(courseDto.getCover());
        order.setTeacherName(courseDto.getTeacherName());
        Long totalFee = courseDto.getPrice().multiply(new BigDecimal(100)).longValue();
        order.setTotalFee(totalFee);//分
        order.setMemberId(memberId);
        order.setMobile(memberDto.getMobile());
        order.setNickname(memberDto.getNickname());
        order.setStatus(0);//未支付
        order.setPayType(1);//微信支付
        baseMapper.insert(order);
        return order.getId();
    }

    @Override
    public Order getByOrderId(String orderId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId)
                .eq("member_id",memberId);
        Order order = baseMapper.selectOne(queryWrapper);
        return order;
    }

    @Override
    public Boolean isBuyByCourseId(String courseId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("member_id", memberId)
                .eq("course_id", courseId)
                .eq("status", 1);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count.intValue() > 0;
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);
        Order order = baseMapper.selectOne(queryWrapper);
        return order;
    }

    @Override
    public List<Order> selectByMemberId(String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.eq("member_id",memberId);
        List<Order> orderList = baseMapper.selectList(queryWrapper);
        return orderList;
    }

    @Override
    public boolean removeByIdAndMember(String orderId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId);
        queryWrapper.eq("member_id",memberId);
        return this.remove(queryWrapper);
    }

    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //更新订单状态
        String orderNo = map.get("out_trade_no");
        //Order order = this.getOrderByOrderNo(orderNo);//select
        //order.setStatus(1);//支付成功
        //baseMapper.updateById(order);//update
        //优化：只有一条update sql语句
        baseMapper.updateStatusByOrderNo(orderNo);
        System.out.println("更新状态成功");
        // 写入支付日志
        //记录支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(orderNo);
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(Long.parseLong(map.get("total_fee")));//总金额(分)
        payLog.setTradeState(map.get("result_code"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(new Gson().toJson(map));
        payLogMapper.insert(payLog);

        // 增加课程销量
        Order order = this.getOrderByOrderNo(orderNo);
        eduCourseService.updateBuyCountById(order.getCourseId());
    }

    @Override
    public boolean queryPayStatus(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);
        Order order = baseMapper.selectOne(queryWrapper);
        return order.getStatus() == 1;
    }


}
