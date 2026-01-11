package com.kmbeast.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("health_goal")
public class HealthGoal {
    private Integer id;
    private Integer userId;
    /**
     * 1-减肥，2-增肌
     */
    private Integer type;
    /**
     * 当前数值（体重或体脂）
     */
    private Double currentValue;
    /**
     * 目标数值
     */
    private Double targetValue;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 0-进行中，1-已完成，2-未完成
     */
    private Integer status;

    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}