package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.vo.ChapterVO;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVO;
import com.atguigu.guli.service.edu.entity.vo.CourseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Helen
 * @since 2020-09-19
 */
public interface CourseService extends IService<Course> {


    String saveCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseById(String id);

    void updateCourseInfo(CourseInfoForm courseInfoForm);

    Page<CourseVO> selectPage(Long page, Long limit, CourseQuery courseQuery);

    boolean removeCoverById(String id);

    boolean removeCourseById(String id);

    CoursePublishVO CoursePublishVoById(String id);

    boolean publishCourseById(String id);


}
