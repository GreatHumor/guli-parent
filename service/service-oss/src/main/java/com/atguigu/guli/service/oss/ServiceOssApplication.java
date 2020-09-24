package com.atguigu.guli.service.oss;

import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan({"com.atguigu.guli"})
@Slf4j
public class ServiceOssApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(ServiceOssApplication.class);
        } catch (Exception e) {
            e.printStackTrace();
            // （推荐）如果项目中存在日志框架，可以通过日志框架打印
            log.debug("the exception is {}", e.getMessage(), e);
        }

    }
}
