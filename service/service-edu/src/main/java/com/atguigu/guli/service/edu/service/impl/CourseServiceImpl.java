package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.*;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.query.WebCourseQuery;
import com.atguigu.guli.service.edu.entity.vo.ChapterVO;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVO;
import com.atguigu.guli.service.edu.entity.vo.CourseVO;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.atguigu.guli.service.edu.feign.OssFileService;
import com.atguigu.guli.service.edu.mapper.*;
import com.atguigu.guli.service.edu.service.CourseDescriptionService;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-09-19
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private ChapterMapper chapterMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CourseCollectMapper courseCollectMapper;
    @Autowired
    private OssFileService ossFileService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {
        // 保存Course
        Course course = new Course();
//        course.setTitle(courseInfoForm.getTitle());
        BeanUtils.copyProperties(courseInfoForm,course);
        course.setStatus(Course.COURSE_DRAFT);
        baseMapper.insert(course);
        // 保存CourseDescription
        CourseDescription description = new CourseDescription();
        description.setDescription(courseInfoForm.getDescription());
        description.setId(course.getId());
        courseDescriptionMapper.insert(description);
        return course.getId();
    }

    @Override
    public CourseInfoForm getCourseById(String id) {
        // 查询course
        Course course = baseMapper.selectById(id);
        System.out.println(course);
        if (course == null){
            return null;
        }
        // 查询course_description
        CourseDescription courseDescription = courseDescriptionMapper.selectById(id);
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course,courseInfoForm);
        if (courseDescription != null){
            courseInfoForm.setDescription(courseDescription.getDescription());
        }
        return courseInfoForm;
    }

    @Override
    public void updateCourseInfo(CourseInfoForm courseInfoForm) {
        // 更新Course
        Course course = new Course();
//        course.setTitle(courseInfoForm.getTitle());
        BeanUtils.copyProperties(courseInfoForm,course);
        baseMapper.updateById(course);
        // 跟新CourseDescription
        CourseDescription description = new CourseDescription();
        description.setDescription(courseInfoForm.getDescription());
        description.setId(course.getId());
        int result = courseDescriptionMapper.updateById(description);
        if (result == 0){
            courseDescriptionMapper.insert(description);
        }

    }

    @Override
    public Page<CourseVO> selectPage(Long page, Long limit, CourseQuery courseQuery) {


        String subjectParentId = courseQuery.getSubjectParentId();
        String teacherId = courseQuery.getTeacherId();
        String title = courseQuery.getTitle();
        String subjectId = courseQuery.getSubjectId();
        QueryWrapper<CourseVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("c.publish_time");

        if (!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("c.subject_id",subjectId);
        }
        if (!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.eq("c.subject_parent_id",subjectParentId);
        }
        if (!StringUtils.isEmpty(title)){
            queryWrapper.like("c.title",title);
        }
        if (!StringUtils.isEmpty(teacherId)){
            queryWrapper.eq("c.teacher_id",teacherId);
        }


        Page<CourseVO> pageParam = new Page<>(page, limit);
        List<CourseVO> records= baseMapper.selectPageByCourseQuery(pageParam,queryWrapper);
        pageParam.setRecords(records);
        return pageParam;
    }

    @Override
    public boolean removeCoverById(String id) {
        Course course = baseMapper.selectById(id);
        if (course !=null){
            String cover = course.getCover();
            if(!StringUtils.isEmpty(cover)){
                // 删除图片
                R r = ossFileService.removeFile(cover);
                return r.getSuccess();
            }
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeCourseById(String id) {

        // 收藏信息：course_collect
        QueryWrapper<CourseCollect> courseCollectQueryWrapper = new QueryWrapper<>();
        courseCollectQueryWrapper.eq("course_id",id);
        courseCollectMapper.delete(courseCollectQueryWrapper);

        //课时信息：video
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", id);
        videoMapper.delete(videoQueryWrapper);

        //章节信息：chapter
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", id);
        chapterMapper.delete(chapterQueryWrapper);

        //课程详情：course_description
        courseDescriptionMapper.deleteById(id);

        // 课程信息
        return this.removeById(id);
    }

    @Override
    public CoursePublishVO CoursePublishVoById(String id) {
        return baseMapper.selectCoursePublishVoById(id);
    }

    @Override
    public boolean publishCourseById(String id) {
        Course course = new Course();
        course.setId(id);
        course.setPublishTime(new Date());
        course.setStatus(Course.COURSE_NORMAL);
        return this.updateById(course);
    }

    @Override
    public List<Course> selectCourseByQuery(WebCourseQuery webCourseQuery) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        // 查询已发布的课程
        queryWrapper.eq("status",Course.COURSE_NORMAL);

        String subjectParentId = webCourseQuery.getSubjectParentId();
        String subjectId = webCourseQuery.getSubjectId();
        String priceSort = webCourseQuery.getPriceSort();
        String publishTimeSort = webCourseQuery.getPublishTimeSort();
        String buyCountSort = webCourseQuery.getBuyCountSort();


        if (!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("subject_id",subjectId);
        }
        if (!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.eq("subject_parent_id",subjectParentId);
        }
        if (!StringUtils.isEmpty(buyCountSort)){
            queryWrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(publishTimeSort)){
            queryWrapper.orderByDesc("publish_time");
        }
        if (!StringUtils.isEmpty(priceSort)){
            if ("1".equals(priceSort)){
                queryWrapper.orderByAsc("price");
            } else {
                queryWrapper.orderByDesc("price");
            }
        }

        return baseMapper.selectList(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WebCourseVo selectWebCourseVoById(String id) {
        //浏览数+1
        baseMapper.updateViewCountById(id);
        //获取课程信息
        return baseMapper.selectWebCourseVoById(id);
    }
}
