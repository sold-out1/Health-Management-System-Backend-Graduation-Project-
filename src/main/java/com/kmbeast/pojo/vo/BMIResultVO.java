package com.kmbeast.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BMI测算结果VO类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BMIResultVO {
    /**
     * 测算结果值
     */
    private Double result;
    /**
     * 指标情况
     */
    private String info;
}
