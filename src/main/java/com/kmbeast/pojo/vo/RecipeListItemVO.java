package com.kmbeast.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 食谱列表VO类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeListItemVO {
    /**
     * ID
     */
    private Integer id;
    /**
     * 食谱名
     */
    private String name;
    /**
     * 封面
     */
    private String cover;
    /**
     * 发布者
     */
    private String username;
    /**
     * 发布者用户头像
     */
    private String avatar;
    /**
     * 是否已经审核
     */
    private Boolean isAudit;
    /**
     * 是否公开
     */
    private Boolean isPublic;
    /**
     * 发布时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
