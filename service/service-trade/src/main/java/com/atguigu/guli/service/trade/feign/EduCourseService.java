package com.atguigu.guli.service.trade.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.feign.fallback.EduCourseServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "service-edu",fallback = EduCourseServiceFallBack.class)
public interface EduCourseService {

    @GetMapping("/api/edu/course/get-course-dto/{courseId}")
    R getCourseDtoById(@PathVariable(value = "courseId") String courseId);

    @PutMapping("/api/edu/course/update-buy-count/{id}")
    R updateBuyCountById(@PathVariable("id") String id);
}
