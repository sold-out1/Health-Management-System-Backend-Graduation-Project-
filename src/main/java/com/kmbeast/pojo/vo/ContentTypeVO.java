package com.kmbeast.pojo.vo;

import lombok.Data;

/**
 * 内容类型VO
 */
@Data
public class ContentTypeVO {
    /**
     * ID
     */
    private Integer id;
    /**
     * 类型名
     */
    private String typeName;
    /**
     * 类型对应的内容数量
     */
    private Integer count;
}
