package com.kmbeast.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流量类型枚举
 */
@Getter
@AllArgsConstructor
public enum FlowIndexEnum {

    //1：展现；2：浏览；3：点赞；4：收藏；5：停留

    FLOW_INDEX_1(1, "展现"),
    FLOW_INDEX_2(2, "浏览"),
    FLOW_INDEX_3(3, "点赞"),
    FLOW_INDEX_4(4, "收藏"),
    FLOW_INDEX_5(5, "停留");

    private final Integer id; // ID
    private final String type; // 类型

}
