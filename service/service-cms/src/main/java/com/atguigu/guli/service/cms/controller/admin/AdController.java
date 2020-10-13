package com.atguigu.guli.service.cms.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.entity.vo.AdVo;
import com.atguigu.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 广告推荐 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-09-08
 */
@CrossOrigin //解决跨域问题
@Api(tags = "广告推荐管理")
@RestController
@RequestMapping("/admin/cms/ad")
@Slf4j
public class AdController {

    @Autowired
    private AdService adService;

    @ApiOperation("新增推荐")
    @PostMapping("save")
    public R save(@ApiParam(value = "推荐对象", required = true) @RequestBody Ad ad) {

        boolean result = adService.save(ad);
        //更新缓存

        if (result) {
            return R.ok().message("保存成功");
        } else {
            return R.error().message("保存失败");
        }
    }

    @ApiOperation("更新推荐")
    @PutMapping("update")
    public R updateById(@ApiParam(value = "讲师推荐", required = true) @RequestBody Ad ad) {
        boolean result = adService.updateById(ad);
        //更新缓存

        if (result) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id获取推荐信息")
    @GetMapping("get/{id}")
    public R getById(@ApiParam(value = "推荐ID", required = true) @PathVariable String id) {
        Ad ad = adService.getById(id);
        if (ad != null) {
            return R.ok().data("item", ad);
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation(value = "根据ID删除推荐")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam(value = "推荐ID", required = true) @PathVariable String id) {

        //删除图片
        boolean result = adService.removeAdImageById(id);
        if (!result) {
            log.warn("广告图片删除失败，广告id:" + id);
        }
        //删除推荐
        boolean b = adService.removeById(id);
        if (b) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("推荐分页列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(@ApiParam(value = "当前页码", required = true) @PathVariable Long page,
                      @ApiParam(value = "每页记录数", required = true) @PathVariable Long limit) {
        Page<AdVo> pageParam = new Page<>(page, limit);
        adService.selectPage(pageParam);
        long total = pageParam.getTotal();
        List<AdVo> records = pageParam.getRecords();
        return R.ok().data("rows", records).data("total", total);
    }
}

