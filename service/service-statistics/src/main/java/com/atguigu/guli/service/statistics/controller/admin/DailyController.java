package com.atguigu.guli.service.statistics.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.statistics.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author luozihan
 * @since 2020-10-19
 */
@Api(tags = "统计分析管理")
@RestController
@RequestMapping("/admin/statistics/daily")

public class DailyController {

    @Autowired
    private DailyService dailyService;

    @ApiOperation("生成统计记录")
    @PostMapping("create/{day}")
    public R createStatisticByDay(
            @ApiParam(value = "统计日期")
            @PathVariable String day){
        dailyService.createStatisticsByDay(day);
        return R.ok().message("数据统计生成成功");
    }
}

