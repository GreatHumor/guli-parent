package com.atguigu.guli.service.cms.entity;

import com.atguigu.guli.service.base.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 广告推荐
 * </p>
 *
 * @author Helen
 * @since 2020-09-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cms_ad")
public class Ad extends BaseEntity /*implements Serializable */{

    private static final long serialVersionUID=1L;

    /**
     * 标题
     */
    private String title;

    /**
     * 类型ID
     */
    private String typeId;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 背景颜色
     */
    private String color;

    /**
     * 链接地址
     */
    private String linkUrl;

    /**
     * 排序
     */
    private Integer sort;


}
