package com.kmbeast.pojo.vo;

import lombok.Data;

/**
 * 用户对于内容的评分VO类
 */
@Data
public class FlowIndexScoreVO {
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 内容ID
     */
    private Integer contentId;
    /**
     * 评分
     */
    private Double score;
}
