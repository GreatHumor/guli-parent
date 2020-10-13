package com.atguigu.guli.service.cms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.atguigu.guli.service.base.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 推荐位
 * </p>
 *
 * @author Helen
 * @since 2020-09-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cms_ad_type")
public class AdType extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 标题
     */
    private String title;


}
