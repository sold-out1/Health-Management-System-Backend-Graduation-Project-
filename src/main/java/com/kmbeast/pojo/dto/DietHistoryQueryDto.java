package com.kmbeast.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 饮食记录查询条件类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DietHistoryQueryDto extends QueryDto{
    /**
     * 用户ID，外键，与用户表关联
     */
    private Integer userId;
    /**
     * 食谱ID，外键，与食谱表关联
     */
    private Integer recipeId;
    /**
     * 备注
     */
    private String detail;
}
