package com.atguigu.guli.service.statistics.task;

import com.atguigu.guli.service.statistics.service.DailyService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledTask {

    @Autowired
    private DailyService dailyService;

    @Scheduled(cron = "0 22 19 1-30 1-12 ? ")//注意只支持6位表达式
    public void taskGenneratedStatisticsData(){
        // 获取上一天的日期
        String day = new DateTime().minusDays(1).toString("yyyy-MM-dd");
        dailyService.createStatisticsByDay(day);
        log.info("taskGennerate统计完毕");
    }


}
