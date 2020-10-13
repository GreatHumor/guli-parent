package com.atguigu.guli.service.cms.feign.fallback;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author helen
 * @since 2020/9/8
 */
@Service
@Slf4j
public class OssFileServiceFallBack implements OssFileService {
    @Override
    public R removeFile(String url) {
        log.warn("远程调用失败，服务熔断");
        return R.error();
    }
}