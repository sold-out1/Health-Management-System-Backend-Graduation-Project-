package com.kmbeast.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 食谱查询条件类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecipeQueryDto extends QueryDto {
    /**
     * 食谱分类ID，外键，资讯分类通过后端枚举类进行管理
     */
    private Integer typeId;
    /**
     * 名称
     */
    private String name;
    /**
     * 用户ID，外键，关联的是用户表
     */
    private Integer userId;
    /**
     * 是否审核
     */
    private Boolean isAudit;
    /**
     * 是否公开
     */
    private Boolean isPublic;
}
