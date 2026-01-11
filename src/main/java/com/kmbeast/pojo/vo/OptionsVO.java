package com.kmbeast.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 选择项VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionsVO {
    /**
     * 值
     */
    private Integer value;
    /**
     * 名称
     */
    private String label;

}
