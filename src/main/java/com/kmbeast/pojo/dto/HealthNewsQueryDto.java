package com.kmbeast.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 健康资讯查询条件类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HealthNewsQueryDto extends QueryDto {
    /**
     * 类别ID
     */
    private Integer typeId;
    /**
     * 资讯标题
     */
    private String title;
    /**
     * 排序字段
     */
    private String sortField;
}
