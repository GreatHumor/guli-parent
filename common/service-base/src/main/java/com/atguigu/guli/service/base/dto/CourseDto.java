package com.atguigu.guli.service.base.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseDto {

    private String id; // 课程ID
    private String title; // 课程标题
    private BigDecimal price;// 课程销售价格，设置位0则可免费观看
    private String cover; // 课程封面图片路径
    private String teacherName; // 课程讲师

}