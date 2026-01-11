package com.kmbeast.pojo.vo;

import lombok.Data;

/**
 * 仪表盘静态数据VO类
 */
@Data
public class StaticCountVO {
    /**
     * 用户数
     */
    private Integer userCount;
    /**
     * 模型数
     */
    private Integer modelCount;
    /**
     * 健康资讯数
     */
    private Integer healthNewsCount;
    /**
     * 收录食谱
     */
    private Integer recipeCount;
}
