package com.atguigu.guli.service.edu.feign;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.fallback.OssFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-oss",fallback = OssFileServiceFallBack.class)
public interface OssFileService {

    @GetMapping("/admin/oss/file/test1")
    public R test1();

    @DeleteMapping("/admin/oss/file/remove")
    R removeFile(@RequestBody String avatar);
}
