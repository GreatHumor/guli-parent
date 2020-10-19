package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.fallback.VodMediaServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(value = "service-vod",fallback = VodMediaServiceFallBack.class)
public interface VodMediaService {


    @DeleteMapping("/admin/vod/media/remove/{videoSourceId}")
    R removeVideo(@PathVariable("videoSourceId")String videoSourceId);
}
