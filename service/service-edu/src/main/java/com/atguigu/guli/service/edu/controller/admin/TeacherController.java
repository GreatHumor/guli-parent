package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-09-19
 */
@Api(tags = "讲师管理")
@RestController
@RequestMapping("admin/edu/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation(value = "获取所有讲师列表")
    @GetMapping("list")
    public R listAll(){
        List<Teacher> list = teacherService.list();
        return R.ok().data("teacherlist",list);
    }

    @ApiOperation(value = "根据id删除讲师",notes = "逻辑删除讲师")
    @DeleteMapping("remove/{id}")
    public R removeById(
            @ApiParam(value = "讲师id",required = true)
            @PathVariable String id){
        boolean b = teacherService.removeById(id);
        if (b){
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation(value = "新增讲师")
    @PostMapping("save")
    public R save(@RequestBody Teacher teacher){
        boolean b = teacherService.save(teacher);
        if (b){
            return R.ok().data("item",teacher);
        } else {
            return R.error().message("保存失败");
        }
    }

//    @ApiOperation(value = "获取分页查询所有讲师列表")
//    @GetMapping("list/{page}/{limit}")
//    public R listPage(
//            @ApiParam(value = "当前页码",required = true)
//            @PathVariable Long page,
//            @ApiParam(value = "每页记录数",required = true)
//            @PathVariable Long limit){
//        Page<Teacher> pageParam = new Page<>(page, limit);
//        teacherService.page(pageParam);
//        return R.ok().data("page",pageParam);
//    }

    /**
     * 条件查询
     * @param page
     * @param limit
     * @return
     */
    @ApiOperation(value = "获取分页查询所有讲师列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(
            @ApiParam(value = "当前页码",required = true)
            @PathVariable Long page,
            @ApiParam(value = "每页记录数",required = true)
            @PathVariable Long limit,
            @ApiParam(value = "讲师查询对象",required = false)
            TeacherQuery teacherQuery
    ){
        Page<Teacher> pageParam = new Page<>(page, limit);
        teacherService.selectPage(pageParam,teacherQuery);
        return R.ok().data("page",pageParam);
    }





















}

