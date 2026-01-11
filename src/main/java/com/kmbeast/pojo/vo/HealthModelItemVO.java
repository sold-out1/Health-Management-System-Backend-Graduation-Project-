package com.kmbeast.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthModelItemVO {
    /**
     * 值
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
}
