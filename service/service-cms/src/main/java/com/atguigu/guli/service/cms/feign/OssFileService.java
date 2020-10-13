package com.atguigu.guli.service.cms.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.feign.fallback.OssFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author helen
 * @since 2020/9/8
 */
@FeignClient(value = "service-oss", fallback = OssFileServiceFallBack.class)
public interface OssFileService {
    @DeleteMapping("/admin/oss/file/remove")
    public R removeFile(@RequestBody String url);
}