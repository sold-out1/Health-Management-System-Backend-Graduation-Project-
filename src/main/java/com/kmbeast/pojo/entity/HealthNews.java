package com.kmbeast.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 健康资讯实体，关联数据库 health_news 表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "health_news")
public class HealthNews {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 资讯分类ID，外键，资讯分类通过后端枚举类进行管理
     */
    private Integer typeId;
    /**
     * 封面
     */
    private String cover;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 摘要
     */
    private String summary;
    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
