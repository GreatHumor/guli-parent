package com.atguigu.guli.service.cms.service;

import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.entity.vo.AdVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务类
 * </p>
 *
 * @author Helen
 * @since 2020-09-08
 */
public interface AdService extends IService<Ad> {

    boolean removeAdImageById(String id);

    void selectPage(Page<AdVo> pageParam);

    List<Ad> selectByAdTypeId(String adTypeId);
}
