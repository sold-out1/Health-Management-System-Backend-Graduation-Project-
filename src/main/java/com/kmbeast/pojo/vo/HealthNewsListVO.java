package com.kmbeast.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 健康资讯列表VO
 */
@Data
public class HealthNewsListVO {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 摘要
     */
    private String summary;
    /**
     * 封面
     */
    private String cover;
    /**
     * 展现量
     */
    private Integer showCount;
    /**
     * 点击率
     */
    private Double clickRate;
    /**
     * 阅读量
     */
    private Integer viewCount;
    /**
     * 点赞量
     */
    private Integer upvoteCount;
    /**
     * 收藏量
     */
    private Integer collectionCount;
    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
