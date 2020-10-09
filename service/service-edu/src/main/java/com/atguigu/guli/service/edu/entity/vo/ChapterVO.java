package com.atguigu.guli.service.edu.entity.vo;

import com.atguigu.guli.service.edu.entity.Video;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel("课程章节对象")
@Data
public class ChapterVO {

    @ApiModelProperty(value = "章节ID")
    private String id;
    @ApiModelProperty(value = "章节标题")
    private String title;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "章节下的课时列表")
    private List<VideoVO> children = new ArrayList<>();
}
