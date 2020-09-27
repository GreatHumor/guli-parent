package com.atguigu.guli.service.edu.feign.fallback;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OssFileServiceFallBack implements OssFileService {
    @Override
    public R test1() {
        log.warn("服务已熔断");
        return null;
    }

    @Override
    public R removeFile(String avatar) {
        log.warn("服务已熔断");
        return null;
    }
}
