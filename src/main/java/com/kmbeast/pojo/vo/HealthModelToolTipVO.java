package com.kmbeast.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 健康数据折线图工具配置VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthModelToolTipVO {
    /**
     * 值 ---> ID
     */
    private Integer value;
    /**
     * 名称
     */
    private String label;

    /**
     * 单位
     */
    private String unit;

    /**
     * 简介
     */
    private String detail;
}
