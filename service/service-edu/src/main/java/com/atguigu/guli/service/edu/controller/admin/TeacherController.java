package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.atguigu.guli.service.edu.feign.OssFileService;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-09-19
 */
@CrossOrigin
@Api(tags = "讲师管理")
@RestController
@RequestMapping("admin/edu/teacher")
@Slf4j
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation(value = "获取所有讲师列表")
    @GetMapping("list")
    public R listAll(){
        List<Teacher> list = teacherService.list();
        return R.ok().data("teacherlist",list);
    }

    // 根据id修改
    @ApiOperation("更新讲师")
    @PutMapping("update")
    public R updateById(@ApiParam(value = "讲师对象",required = true) @RequestBody Teacher teacher) {
        boolean b = teacherService.updateById(teacher);
        if(b) {
            return R.ok().message("更新成功");
        }else {
            return R.error().message("数据不存在");
        }
    }

    // 根据左关键字查询列名
    @ApiOperation("根据左关键字查询")
    @GetMapping("list/name/{key}")
    public R selectNameListByKey(
            @ApiParam(value = "查询关键字",required = true)
            @PathVariable String key){
        List<Map<String, Object>> nameList= teacherService.selectNameListByKey(key);
        return R.ok().data("nameList",nameList);

    }

    // 根据id查询
    @ApiOperation("根据id查询讲师信息")
    @GetMapping("get/{id}")
    public R getById(@ApiParam(value = "讲师ID",required = true) @PathVariable String id) {
        Teacher teacher = teacherService.getById(id);
        if(teacher !=null) {
            return R.ok().data("item",teacher);
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation(value = "根据id删除讲师",notes = "逻辑删除讲师")
    @DeleteMapping("remove/{id}")
    public R removeById(
            @ApiParam(value = "讲师id",required = true)
            @PathVariable String id){
        // 删除头像
        boolean b1 = teacherService.removeAvatarById(id);
        if (!b1){
            log.warn("讲师头像删除失败,讲师id: "+id);
            System.out.println("讲师头像删除失败,讲师id: "+id);
        }

        // 删除讲师
        boolean b = teacherService.removeById(id);
        if (b){
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation(value = "根据id列表删除讲师")
    @DeleteMapping("batch-remove")
    public R removeRows(
            @ApiParam(value = "讲师id列表",required = true)
            @RequestBody List<String> idList){
        boolean b = teacherService.removeByIds(idList);
        if (b){
            return R.ok().message("批量删除成功");
        } else {
            return R.ok().message("批量删除失败");
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


    @Autowired
    OssFileService ossFileService;

    @GetMapping("/test1/{id}")
    public R test(){
        System.out.println("edu teacher test");
        ossFileService.test1();
        return R.ok();
    }


















}

