package com.kmbeast.pojo.vo;

import com.kmbeast.pojo.entity.HealthRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 健康记录VO类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthRecordVO extends HealthRecord {
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 健康模型名称
     */
    private String healthModelName;
    /**
     * 健康模型单位
     */
    private String healthModelUnit;
    /**
     * 健康模型阈值
     */
    private String normalValue;
}
