package com.atguigu.guli.service.ucenter.service.impl;

import com.atguigu.guli.common.util.MD5;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.form.RegisterForm;
import com.atguigu.guli.service.ucenter.mapper.MemberMapper;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-10-12
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Override
    public void register(RegisterForm registerForm) {
        // 用户是否被注册
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",registerForm.getMobile());
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0){
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }

        // 会员注册
        Member member = new Member();
        member.setNickname(registerForm.getNickname());
        member.setMobile(registerForm.getMobile());
        member.setPassword(MD5.encrypt(registerForm.getPassword()));
        member.setAvatar("https://guli-file-helen.oss-cn-beijing.aliyuncs.com/avatar/default.jpg");
        member.setDisabled(false);

        baseMapper.insert(member);
    }
}
