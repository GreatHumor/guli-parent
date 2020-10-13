package com.atguigu.guli.service.vod.service.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "阿里云视频点播")
@CrossOrigin // 跨域
@RestController
@RequestMapping("/api/vod/media")
@Slf4j
public class ApiMediaController {

    @Autowired
    private VideoService videoService;

    @GetMapping("get-play-auth/{videoSourceId}")
    public R getPlayAuth(
            @ApiParam(value = "阿里云视频文件的id",required = true)
            @PathVariable String videoSourceId){
        // Todo 补充视频播放功能
        return null;
    }
}
