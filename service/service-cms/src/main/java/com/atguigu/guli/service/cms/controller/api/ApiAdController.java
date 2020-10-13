package com.atguigu.guli.service.cms.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Api(tags = "广告推荐")
@RestController
@Slf4j
@RequestMapping("/api/cms/ad")
public class ApiAdController {

    @Autowired
    private AdService adService;

    @ApiOperation("根据推荐位id显示广告")
    @GetMapping("list/{adTypeId}")
    public R listByAdTypeId(
            @ApiParam(value = "推荐位id",required = true)
            @PathVariable String adTypeId){
        List<Ad> ads = adService.selectByAdTypeId(adTypeId);
        return R.ok().data("items",ads);
    }

}