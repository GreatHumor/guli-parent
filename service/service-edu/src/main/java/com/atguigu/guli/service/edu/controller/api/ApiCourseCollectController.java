package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.vo.CourseCollectVo;
import com.atguigu.guli.service.edu.service.CourseCollectService;
import com.atguigu.guli.service.edu.service.CourseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/edu/course-collect")
@Slf4j
public class ApiCourseCollectController {

    @Autowired
    private CourseCollectService courseCollectService;

    @Autowired
    private CourseService courseService;

    @ApiOperation("根据课程id查询课程信息")
    @GetMapping("get-course-dto/{courseId}")
    public R getCourseDtoById(@PathVariable String courseId){
        CourseDto courseDto = courseService.getCourseDtoById(courseId);
        return R.ok().data("courseDto",courseDto);
    }


    @ApiOperation("查询课程是否被收藏")
    @GetMapping("auth/is-collect/{courseId}")
    public R isCollect(
            @ApiParam(value = "课程id",required = true)
            @PathVariable String courseId,
            HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        boolean isCollect = courseCollectService.isCollect(courseId,memberId);
        return R.ok().data("isCollect",isCollect);
    }

    @ApiOperation("收藏课程")
    @PostMapping("auth/save/{courseId}")
    public R save(
            @ApiParam(value = "课程id",required = true)
            @PathVariable String courseId,
            HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        courseCollectService.saveCourseCollect(courseId,memberId);
        return R.ok();
    }

    @ApiOperation("获取收藏列表")
    @GetMapping("auth/list")
    public R listCollect(HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        List<CourseCollectVo> collectVoList = courseCollectService.selectListByMemberId(memberId);
        return R.ok().data("items",collectVoList);
    }

    @ApiOperation(value = "取消收藏课程")
    @DeleteMapping("auth/remove/{courseId}")
    public R removeCollect(
            @ApiParam(value = "课程id",required = true)
            @PathVariable String courseId,
            HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        boolean result = courseCollectService.removeCourseCollect(courseId,memberId);
        if (result){
            return R.ok().message("已取消收藏");
        } else {
            return R.ok().message("取消收藏失败");
        }

    }

}
