package com.kmbeast.pojo.em;

import com.kmbeast.pojo.vo.OptionsVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 分类枚举
 */
@Getter
@AllArgsConstructor
public enum TypeEnum {

    // 食谱模块类别
    RECIPE_1(1, "RECIPE", "减脂"),
    RECIPE_2(2, "RECIPE", "增肌"),

    // 健康资讯模块类别
    NEWS_1(1, "NEWS", "有氧运动"),
    NEWS_2(2, "NEWS", "健康晨跑"),
    NEWS_3(3, "NEWS", "睡眠健康"),
    NEWS_4(4, "NEWS", "心理健康"),
    NEWS_5(5, "NEWS", "饮食指南");

    private final Integer id; // ID
    private final String module; // 所处的模块
    private final String name; // 分类名

    public static List<OptionsVO> getOptionsByModule(String module) {
        List<OptionsVO> options = new ArrayList<>();
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.getModule().equals(module)) {
                OptionsVO optionsVO = new OptionsVO(
                        typeEnum.getId(),
                        typeEnum.getName()
                );
                options.add(optionsVO);
            }
        }
        return options;
    }

    public static String getNameByModule(String module, Integer id) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (Objects.equals(typeEnum.getModule(), module) && typeEnum.getId().equals(id)) {
                return typeEnum.getName();
            }
        }
        return null;
    }

}
