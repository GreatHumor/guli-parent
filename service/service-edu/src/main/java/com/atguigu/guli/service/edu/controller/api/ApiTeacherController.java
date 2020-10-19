package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.generator.config.IFileCreate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "讲师列表")

@RestController
@RequestMapping("api/edu/teacher")
public class ApiTeacherController {
    @Autowired
    private TeacherService teacherService;

    @ApiOperation(value = "获取所有讲师列表")
    @GetMapping("list")
    public R listAll(){
        List<Teacher> list = teacherService.list();
        return R.ok().data("items",list);
    }

    @ApiOperation(value = "获取分页查询讲师列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(
            @ApiParam(value = "当前页码",required = true)
            @PathVariable Long page,
            @ApiParam(value = "每页记录数",required = true)
            @PathVariable Long limit){
        Page<Teacher> pageParam = new Page<>(page, limit);
        teacherService.page(pageParam);
        return R.ok().data("page",pageParam);
    }

    @ApiOperation(value = "获取讲师")
    @GetMapping("get/{id}")
    public R getTeacher(
            @ApiParam(value = "讲师id",required = true)
            @PathVariable String id){
        Map<String,Object> map = teacherService.selectTeacherInfoById(id);
        return R.ok().data(map);
    }

}
