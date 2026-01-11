package com.kmbeast.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 饮食记录实体，关联数据库 diet_history 表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "diet_history")
public class DietHistory {
    /**
     * 主键ID
     */
    private Integer id;
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
    /**
     * 摄入值
     */
    private Double value;
    /**
     * 记录时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
