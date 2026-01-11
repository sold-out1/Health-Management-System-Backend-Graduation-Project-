package com.kmbeast.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模型统计VO类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthModelCountVO {
    /**
     * 公共模型数
     */
    private Integer globalModelCount;
    /**
     * 私人模型数
     */
    private Integer privateModelCount;
}
