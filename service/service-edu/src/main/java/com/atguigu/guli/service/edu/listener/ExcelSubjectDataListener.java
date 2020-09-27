package com.atguigu.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.ExcelSubjectData;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {

    // @Autowired
    private SubjectMapper subjectMapper;

    @Override
    public void invoke(ExcelSubjectData data, AnalysisContext context) {

        // 处理读进来的数据
        String levelOneTitle = data.getLevelOneTitle();
        String levelTwoTitle = data.getLevelTwoTitle();


        // 判断一级分类是否重复
        Subject subjectLevelOne = this.getByTitle(levelOneTitle);
        String parentId = null;
        if (subjectLevelOne == null){
            // 构建并存储一级类别
            Subject subject = new Subject();
            subject.setTitle(levelOneTitle);
            subject.setParentId("0");
            subjectMapper.insert(subject);
            parentId = subject.getId();
        } else {
            parentId = subjectLevelOne.getId();
        }


        // 查询二级类别是否存在
        Subject levelTwoFromDB = this.getSubSubjectByTitle(parentId,levelTwoTitle);
        if (levelTwoFromDB == null){
            // 构建并存储二级类别
            Subject subjectLevelTwo = new Subject();
            subjectLevelTwo.setTitle(levelTwoTitle);
            subjectLevelTwo.setParentId(parentId);
            subjectMapper.insert(subjectLevelTwo);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    private Subject getByTitle(String tile){
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",tile);
        queryWrapper.eq("parent_id","0");// 一级分类
        return subjectMapper.selectOne(queryWrapper);
    }

    /**
     * 相同的一级类别下是否有相同的二级类别
     * @param parentId
     * @param tile
     * @return
     */
    private Subject getSubSubjectByTitle(String parentId, String tile){
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",tile);
        queryWrapper.eq("parent_id",parentId);// 一级分类
        return subjectMapper.selectOne(queryWrapper);
    }











}
