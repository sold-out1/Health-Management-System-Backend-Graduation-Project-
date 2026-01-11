package com.kmbeast.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 模型权限枚举
 */
@Getter
@AllArgsConstructor
public enum IsGlobalEnum {
    PUBLIC(false),
    PRIVATE(true);

    private final Boolean status; // 状态
}
