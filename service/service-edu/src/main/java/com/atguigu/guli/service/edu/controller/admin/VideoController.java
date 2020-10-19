package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-09-19
 */
@Api(tags = "课时管理")
@Slf4j
@RestController
@RequestMapping("/admin/edu/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @ApiOperation("上传课时")
    @PostMapping("save")
    public R save(
            @ApiParam(value = "课时对象",required = true)
            @RequestBody Video video){
        boolean result= videoService.save(video);
        if (result){
            return R.ok().message("上传课时成功");
        } else {
            return R.ok().message("上传课时失败");
        }
    }

    @ApiOperation("根据id删除课时")
    @DeleteMapping("remove/{id}")
    public R deleteById(
            @ApiParam(value = "课时id",required = true)
            @PathVariable String id){
        boolean result = videoService.removeById(id);
        videoService.removeMediaVideoById(id);
        if (result){
            return R.ok().message("删除课时成功");
        } else {
            return R.ok().message("删除课时失败");
        }
    }

    @ApiOperation("根据id查询课时")
    @GetMapping("get/{id}")
    public R getById(
            @ApiParam(value = "课时id",required = true)
            @PathVariable String id){
        Video video = videoService.getById(id);
        if (video !=null){
            return R.ok().data("item",video);
        } else {
            return R.ok().message("数据不存在");
        }
    }

    @ApiOperation("根据id修改课程")
    @PutMapping("update")
    public R updateById(
            @ApiParam(value = "课时对象",required = true)
            @RequestBody Video video){
        boolean result = videoService.updateById(video);
        if (result){
            return R.ok().message("课时修改成功");
        } else {
            return R.ok().message("数据不存在");
        }
    }


}

