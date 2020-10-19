package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.vo.ChapterVO;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVO;
import com.atguigu.guli.service.edu.entity.vo.CourseVO;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-09-19
 */
@Api(tags="课程管理")
//@CrossOrigin
@RestController
@RequestMapping("/admin/edu/course")
@Slf4j
public class CourseController {

    @Autowired
    private CourseService courseService;

    @ApiOperation("保存课程信息")
    @PostMapping("save-course-info")
    public R saveCourseInfo(
            @ApiParam(value = "课程基本信息", required = true)
            @RequestBody CourseInfoForm courseInfoForm) {
        String courseId = courseService.saveCourseInfo(courseInfoForm);
        return R.ok().data("courseId", courseId);
    }

    @ApiOperation("根据课程id获取课程信息")
    @GetMapping("course-info/{id}")
    public R getCourseById(
            @ApiParam(value = "课程id", required = true)
            @PathVariable String id) {
        CourseInfoForm courseInfoForm = courseService.getCourseById(id);
        return R.ok().data("courseInfoForm", courseInfoForm);
    }

    @ApiOperation("更新课程信息")
    @PutMapping("update-course-info")
    public R updateCourseInfo(
            @ApiParam(value = "课程基本信息", required = true)
            @RequestBody CourseInfoForm courseInfoForm) {
        courseService.updateCourseInfo(courseInfoForm);
        return R.ok().message("更新成功");
    }

    @ApiOperation("分页课程列表")
    @GetMapping("list/{page}/{limit}")
    public R list(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(value = "页码大小", required = true)
            @PathVariable Long limit,
            @ApiParam(value = "查询对象", required = true)
                    CourseQuery courseQuery) {

        Page<CourseVO> pageModel = courseService.selectPage(page, limit, courseQuery);
        List<CourseVO> records = pageModel.getRecords();
        long total = pageModel.getTotal();
        return R.ok().data("total",total).data("rows",records);

    }

    @ApiOperation("根据id删除课程")
    @DeleteMapping("remove/{id}")
    public R removeById(
            @ApiParam(value = "课程id",required = true)
            @PathVariable String id){
        //TODO 删除视频：VOD
        //在此处调用vod中的删除视频文件的接口

        //删除封面：OSS
        boolean b = courseService.removeCoverById(id);
        if (!b){
            log.warn("删除课程封面失败，课程id:"+id);
        }
        // 删除课程
        boolean result = courseService.removeCourseById(id);
        if ( !result){
            return R.error().message("数据不存在");
        } else {
            return R.ok().message("删除成功");
        }
    }

    @ApiOperation("根据id获取课程发布信息")
    @GetMapping("course-publish/{id}")
    public R getCoursePublishVoById(
            @ApiParam(value = "课程id",required = true)
            @PathVariable String id){
        CoursePublishVO coursePublishVO = courseService.CoursePublishVoById(id);
        if (coursePublishVO != null){
            return R.ok().data("item",coursePublishVO);
        } else {
            return R.ok().message("数据不存在");
        }
    }

    @ApiOperation("根据id发布课程")
    @PutMapping("publish-course/{id}")
    public R publishCourseById(
            @ApiParam(value = "课程ID",required = true)
            @PathVariable String id){
        boolean result = courseService.publishCourseById(id);
        if (result){
            return R.ok().message("发布成功");
        } else {
            return R.error().message("数据不存在");
        }

    }


}

