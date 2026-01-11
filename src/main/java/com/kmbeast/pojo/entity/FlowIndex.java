package com.kmbeast.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 流量指标实体，关联数据库 flow_index 表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "flow_index")
public class FlowIndex {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 流量类型（1：展现；2：浏览；3：点赞；4：收藏；5：停留）
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
     * 停留时间，当类型是【停留】时，这个字段才需要设置
     */
    private Long times;
    /**
     * 用户ID，外键，关联的是用户表
     */
    private Integer userId;
    /**
     * 记录时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
