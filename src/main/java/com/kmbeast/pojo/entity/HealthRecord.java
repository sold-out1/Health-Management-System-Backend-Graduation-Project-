package com.kmbeast.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 健康记录实体，关联数据库 health_record 表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "health_record")
public class HealthRecord {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 用户ID，外键，与数据库表关联
     */
    private Integer userId;
    /**
     * 健康模型ID
     */
    private Integer healthModelId;
    /**
     * 记录的值
     */
    private Double value;
    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
