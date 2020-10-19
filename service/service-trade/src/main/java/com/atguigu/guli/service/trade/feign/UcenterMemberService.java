package com.atguigu.guli.service.trade.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.feign.fallback.UcenterMemberServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-ucenter",fallback = UcenterMemberServiceFallBack.class)
public interface UcenterMemberService {

    @GetMapping("/api/ucenter/member/get-member-dto/{memberId}")
    R getMemberDtoById(@PathVariable(value = "memberId")String memberId);
}
