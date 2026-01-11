package com.kmbeast.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 食谱实体，关联数据库 recipe 表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "recipe")
public class Recipe {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 食谱分类ID，外键，资讯分类通过后端枚举类进行管理
     */
    private Integer typeId;
    /**
     * 封面
     */
    private String cover;
    /**
     * 名称
     */
    private String name;
    /**
     * 内容
     */
    private String content;
    /**
     * 用户ID，外键，关联的是用户表
     */
    private Integer userId;
    /**
     * 是否是公开食谱，如果是公开食谱，需要管理员审核，私人食谱就是能自己用
     */
    private Boolean isPublic;
    /**
     * 是否审核
     */
    private Boolean isAudit;
    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
