package com.kmbeast.pojo.vo;

import com.kmbeast.pojo.entity.HealthNews;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 健康资讯VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HealthNewsVO extends HealthNews {
    /**
     * 类别名
     */
    private String typeName;
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
}
