package com.atguigu.guli.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.ExcelSubjectData;
import com.atguigu.guli.service.edu.entity.vo.SubjectVO;
import com.atguigu.guli.service.edu.listener.ExcelSubjectDataListener;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-09-19
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {


    @Autowired
    SubjectMapper subjectMapper;

    @Override
    public void batchImport(InputStream inputStream) {
        EasyExcel.read(inputStream,
                ExcelSubjectData.class,
                new ExcelSubjectDataListener(subjectMapper)).excelType(ExcelTypeEnum.XLS).sheet().doRead();
    }

    /**
     * 效率高 1个sql，嵌套数据借助map封装，避免使用递归
     * @return
     */
    @Override
    public List<SubjectVO> nestedList() {
        ArrayList<SubjectVO> subjectVOArrayList = new ArrayList<>();

        List<SubjectVO> subjects = subjectMapper.selectSubjectVoList();

        HashMap<String, SubjectVO> map = new HashMap<>();

        for (SubjectVO subject : subjects) {
            String id = subject.getId();
            map.put(id,subject);
        }

        for (SubjectVO cur : subjects) {
            String parentId = cur.getParentId();
            if ("0".equals(parentId)){
                // 说明是顶级节点
                subjectVOArrayList.add(cur);
            }else {
                // 找到父节点，添加到他的children列表中
                SubjectVO curParent = map.get(parentId);
                curParent.getChildren().add(cur);
            }
        }
        return subjectVOArrayList;
    }
}
