package com.kmbeast.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 健康记录查询条件类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HealthRecordQueryDto extends QueryDto {
    /**
     * 用户ID，外键，与数据库表关联
     */
    private Integer userId;
    /**
     * 健康模型ID
     */
    private Integer healthModelId;
    /**
     * 查询天数
     */
    private Integer days;
}
