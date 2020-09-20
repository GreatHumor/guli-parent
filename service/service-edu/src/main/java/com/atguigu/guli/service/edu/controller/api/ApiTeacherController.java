package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.generator.config.IFileCreate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "讲师管理")
@RestController
@RequestMapping("api/edu/teacher")
public class ApiTeacherController {
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
    public R removeById(@PathVariable String id){
        boolean b = teacherService.removeById(id);
        if (b){
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }
}
