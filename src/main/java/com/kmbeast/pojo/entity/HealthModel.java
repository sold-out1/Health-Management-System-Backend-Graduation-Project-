package com.kmbeast.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 健康模型实体，关联数据库 health_model 表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "health_model")
public class HealthModel {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 模型名称
     */
    private String name;
    /**
     * 模型备注
     */
    private String detail;
    /**
     * 模型图标
     */
    private String iconUrl;
    /**
     * 模型单位
     */
    private String unit;
    /**
     * 模型符号
     */
    private String symbol;
    /**
     * 正常阈值
     */
    private String normalValue;
    /**
     * 用户ID，外键，关联的是用户表
     */
    private Integer userId;
    /**
     * 是否是全局模型 （0：false;1:true）
     */
    private Boolean isGlobal;
    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
