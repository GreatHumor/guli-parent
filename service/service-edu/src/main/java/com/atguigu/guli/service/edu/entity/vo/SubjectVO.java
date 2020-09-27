package com.atguigu.guli.service.edu.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectVO {

    @ApiModelProperty(value = "课程分类ID")
    private String id;
    @ApiModelProperty(value = "课程分类名称")
    private String title;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "课程二级分类列表")
    private List<SubjectVO> children = new ArrayList<>();

    @ApiModelProperty(value = "父菜单id")
    private String parentId;
}
