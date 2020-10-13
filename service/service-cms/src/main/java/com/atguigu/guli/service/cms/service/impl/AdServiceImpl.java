package com.atguigu.guli.service.cms.service.impl;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.entity.vo.AdVo;
import com.atguigu.guli.service.cms.feign.OssFileService;
import com.atguigu.guli.service.cms.mapper.AdMapper;
import com.atguigu.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-09-08
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OssFileService ossFileService;

    /**
     * 根据id删除幻灯片图片
     * @param id
     */
    @Override
    public boolean removeAdImageById(String id) {

        Ad ad = baseMapper.selectById(id);
        if(ad != null){
            String imageUrl = ad.getImageUrl();
            if(!StringUtils.isEmpty(imageUrl)){
                //删除图片：调用client方法
                R r = ossFileService.removeFile(imageUrl);
                return r.getSuccess();
            }
        }
        return false;
    }

    @Override
    public void selectPage(Page<AdVo> pageParam) {
        QueryWrapper<AdVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("a.type_id", "a.sort");
        List<AdVo> records = baseMapper.selectPageByQueryWrapper(pageParam, queryWrapper);
        pageParam.setRecords(records);
    }

    @Override
    public List<Ad> selectByAdTypeId(String adTypeId) {
        // 先查询redis中是否存在adList
        List<Ad> adList = null;

        try{
            adList =(List<Ad>)redisTemplate.opsForValue().get("index::adList");
            if (adList != null){
                return adList;
            }
        } catch (Exception e){
            log.warn("redis服务器异常:get index::adList");
        }

        // 如果存在则取出返回
        //如果不存在则从数据库中获取数据
        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort","id");
        queryWrapper.eq("type_id",adTypeId);
        adList = baseMapper.selectList(queryWrapper);
        try{
            // 将数据存入redis
            redisTemplate.opsForValue().set("index::adList",adList,5, TimeUnit.MINUTES);
        } catch (Exception e){
            log.warn("redis服务异常:set index::adList");
        }
        return adList;
    }




}
