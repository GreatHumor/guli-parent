package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVO;
import com.atguigu.guli.service.edu.entity.vo.CourseVO;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Helen
 * @since 2020-09-19
 */
public interface CourseMapper extends BaseMapper<Course> {

    List<CourseVO> selectPageByCourseQuery(
            //mp会自动组装分页参数
            Page<CourseVO> pageParam,
            //mp会自动组装queryWrapper：
            //@Param(Constants.WRAPPER) 和 xml文件中的 ${ew.customSqlSegment} 对应
            @Param(Constants.WRAPPER) QueryWrapper<CourseVO> queryWrapper);

    CoursePublishVO selectCoursePublishVoById(String id);

    WebCourseVo selectWebCourseVoById(String courseId);

    int updateViewCountById(String id);
}
