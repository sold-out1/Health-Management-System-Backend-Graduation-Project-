package com.kmbeast.pojo.vo;

import com.kmbeast.pojo.entity.Recipe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 食谱详情VO类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeVO extends Recipe {
    /**
     * 分类名
     */
    private String typeName;
    /**
     * 发布者用户头像
     */
    private String avatar;
    /**
     * 发布者
     */
    private String username;
}
