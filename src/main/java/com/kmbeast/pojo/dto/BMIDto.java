package com.kmbeast.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BMI测算接收参数类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BMIDto {
    /**
     * 身高
     */
    private Double height;
    /**
     * 体重
     */
    private Double weight;
}
