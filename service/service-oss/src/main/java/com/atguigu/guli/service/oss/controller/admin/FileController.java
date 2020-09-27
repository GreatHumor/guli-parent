package com.atguigu.guli.service.oss.controller.admin;

import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.oss.service.FileService;
import com.atguigu.guli.service.oss.util.OssProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Api(tags = "阿里云文件管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/oss/file")
@Slf4j
public class FileController {

    @Autowired
    private OssProperties ossProperties;

    @Autowired
    private FileService fileService;

    @GetMapping("test")
    public R test(){
        return R.ok().data("ossProperties",ossProperties);
    }

    @ApiOperation("文件上传")
    @PostMapping("upload")
    public R upload(@ApiParam(value = "文件", required = true)
                        @RequestParam("file") MultipartFile file,

                    @ApiParam(value = "模块", required = true)
                        @RequestParam("module") String module) {

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String url = fileService.upload(inputStream, module, originalFilename);
            return R.ok().message("文件上传成功").data("url",url);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR,"oss文件上传模块"+module);
        }
    }

    @ApiOperation("文件删除")
    @DeleteMapping("remove")
    public R removeFile(
            @ApiParam(value = "待删除文件的url",required = true)
            @RequestBody String url){
        System.out.println("头像的url"+url);
        fileService.removeFile(url);
        System.out.println("url文件连接成功");
        return R.ok().message("文件删除成功");
    }

    @GetMapping("/test1")
    public R test1(){
        System.out.println("服务已连接");
        return R.ok().message("已连接");
    }
}
