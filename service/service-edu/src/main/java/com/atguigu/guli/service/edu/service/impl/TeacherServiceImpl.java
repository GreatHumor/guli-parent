package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.atguigu.guli.service.edu.feign.OssFileService;
import com.atguigu.guli.service.edu.mapper.TeacherMapper;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-09-19
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Override
    public void selectPage(Page<Teacher> pageParam, TeacherQuery teacherQuery) {

        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String joinDataBegin = teacherQuery.getJoinDataBegin();
        String joinDataEnd = teacherQuery.getJoinDataEnd();

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");

        if (teacherQuery == null){
            baseMapper.selectPage(pageParam,queryWrapper);
            return;
        }

        if (!StringUtils.isEmpty(name)) {
            //左%会使索引失效
            queryWrapper.likeRight("name", name);
        }

        if (level != null) {
            queryWrapper.eq("level", level);
        }

        if (!StringUtils.isEmpty(joinDataBegin)) {
            queryWrapper.ge("join_date", joinDataBegin);
        }

        if (!StringUtils.isEmpty(joinDataEnd)) {
            queryWrapper.le("join_date",joinDataEnd);
        }

        baseMapper.selectPage(pageParam,queryWrapper);

    }

    @Override
    public List<Map<String, Object>> selectNameListByKey(String key) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name");
        queryWrapper.likeRight("name",key);
        List<Map<String, Object>> list = baseMapper.selectMaps(queryWrapper);
        return list;
    }

    @Autowired
    private OssFileService ossFileService;

    @Override
    public boolean removeAvatarById(String id) {
        Teacher teacher = baseMapper.selectById(id);
        if (teacher != null){
            String avatar = teacher.getAvatar();
            if (!StringUtils.isEmpty(avatar)){
                // 根据url删除头像
                R r= ossFileService.removeFile(avatar);
                return r.getSuccess();
            }
        }
        return false;
    }
}
