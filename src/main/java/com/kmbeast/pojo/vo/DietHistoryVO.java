package com.kmbeast.pojo.vo;

import com.kmbeast.pojo.entity.DietHistory;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 饮食记录的VO类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DietHistoryVO extends DietHistory {
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 食谱名
     */
    private String recipeName;
    /**
     * 食谱封面
     */
    private String recipeCover;
}
