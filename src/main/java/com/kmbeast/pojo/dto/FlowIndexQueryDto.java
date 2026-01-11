package com.kmbeast.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流量类型查询条件类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FlowIndexQueryDto extends QueryDto{

    /**
     * 流量类型（1：展现；2：浏览；3：点赞；4：收藏；5：分享；6：停留）
     */
    private Integer type;
    /**
     * 所处的内容模块
     */
    private String contentModule;
    /**
     * 内容ID，外键，关联所在内容模块下面的ID
     */
    private Integer contentId;
    /**
     * 用户ID，外键，关联的是用户表
     */
    private Integer userId;

}
